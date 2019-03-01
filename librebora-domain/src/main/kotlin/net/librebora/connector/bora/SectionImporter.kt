package net.librebora.connector.bora

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.librebora.connector.bora.SectionImporter.Companion.CONCURRENT_IMPORT_FILES
import net.librebora.connector.bora.model.importer.ImportSectionTask
import net.librebora.connector.bora.model.importer.ImportTaskMetrics
import net.librebora.connector.bora.model.sections.ListRequest
import net.librebora.connector.bora.model.sections.Page
import net.librebora.connector.bora.model.sections.SectionFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/** Imports files from a BORA section.
 *
 * It processes [ImportSectionTask]s using a fan-out to retrieve files from a page in parallel.
 * It lists all pages for a specific date synchronously and it writes out files to a channel
 * so they can be retrieved in parallel.
 *
 * It uses the default coroutine dispatcher, and it spawns [CONCURRENT_IMPORT_FILES] number of
 * parallel coroutines to retrieve files.
 *
 * @property boraClient Client to retrieve sections and files.
 * @constructor Creates a section importer configured to retrieve section pages.
 */
class SectionImporter(private val boraClient: BoraClient) {

    companion object {
        private const val CONCURRENT_IMPORT_FILES: Int = 5
    }

    private val logger: Logger = LoggerFactory.getLogger(SectionImporter::class.java)

    /** Processes an [ImportSectionTask] and returns the task with the next state.
     *
     * If the import process succeed, it terminates the task and reports the import
     * process metrics and files with errors. If there's an error listing the section,
     * it returns a task in error.
     *
     * It receives a function that determines whether a file exists or not in order to
     * avoid unnecessary requests to the BORA API.
     *
     * @param importTask Import task to process.
     * @param fileExists Function to resolve whether a file exists.
     * @param callback Callback to report successfully imported files.
     *
     * @return The import task with the next state.
     */
    fun importSection(
        importTask: ImportSectionTask,
        fileExists: (String) -> Boolean,
        callback: (SectionFile) -> Unit
    ): ImportSectionTask = runBlocking {
        try {
            logger.info("processing import section task: $importTask")

            val importChannel: Channel<String> = Channel()
            val deferredMetrics: Deferred<ImportTaskMetrics> = listSectionAsync(importTask, importChannel)

            val filesInError = (1..CONCURRENT_IMPORT_FILES).map {
                importFilesAsync(
                    importChannel = importChannel,
                    sectionName = importTask.sectionName,
                    fileExists = fileExists,
                    callback = callback
                )
            }.flatMap { importFileOperation ->
                importFileOperation.await()
            }

            importTask.terminate(
                metrics = deferredMetrics.await(),
                filesInError = filesInError
            )
        } catch(cause: Throwable) {
            importTask.error(cause.localizedMessage)
        }
    }

    private inline fun CoroutineScope.importFilesAsync(
        importChannel: Channel<String>,
        sectionName: String,
        crossinline fileExists: (String) -> Boolean,
        crossinline callback: (SectionFile) -> Unit
    ): Deferred<List<String>> = async(CoroutineName("file-importer")) {

        val filesInError: MutableList<String> = mutableListOf()

        logger.info("waiting for files to import")

        for (fileId in importChannel) {
            try {
                if (!fileExists(fileId)) {
                    logger.info("file $fileId does not exist, retrieving from BORA")
                    callback(boraClient.retrieve(sectionName, fileId))
                } else {
                    logger.info("file $fileId already exist")
                }
            } catch (cause: Throwable) {
                logger.error("error retrieving $fileId from BORA", cause)
                filesInError.add(fileId)
            }
        }

        logger.info("import channel closed, files in error: $filesInError")

        filesInError
    }

    private fun CoroutineScope.listSectionAsync(
        task: ImportSectionTask,
        importChannel: Channel<String>
    ): Deferred<ImportTaskMetrics> = async(CoroutineName("section-importer-${task.timestamp()}")) {

        var request: ListRequest = ListRequest.create(
            sectionName = task.sectionName,
            offset = 1,
            itemsPerPage = task.itemsPerPage,
            date = task.date
        )
        var numberOfPages = 0
        var numberOfFiles = 0

        logger.info("starts import process for section ${task.sectionName} date ${task.date}")

        do {
            logger.info("retrieving page ${request.offset} from section ${task.sectionName} for date ${task.date}")
            val page: Page = boraClient.list(request)

            page.items.forEach { item ->
                importChannel.send(item.fileId)
            }

            numberOfPages += 1
            numberOfFiles += page.items.size
            request = request.next(page.sessionId)
        } while (!page.items.isEmpty())

        importChannel.close()

        logger.info("ends import process for section ${task.sectionName} date ${task.date} " +
                "($numberOfPages pages, $numberOfFiles files)")

        ImportTaskMetrics(
            numberOfPages = numberOfPages,
            numberOfFiles = numberOfFiles
        )
    }
}
