package net.borak.domain.bora

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.borak.domain.bora.model.importer.ImportFileResult
import net.borak.domain.bora.model.importer.ImportTask
import net.borak.domain.bora.model.sections.SectionFile
import net.borak.domain.bora.model.sections.ListRequest
import net.borak.domain.bora.model.sections.Page
import net.borak.domain.bora.persistence.ImportTaskDAO

class SectionImporter(private val boraClient: BoraClient,
                      private val importTaskDAO: ImportTaskDAO) {

    private data class ImportSectionResult(val task: ImportTask,
                                           val pages: List<Page>)

    companion object {
        private const val CONCURRENT_IMPORT_TASKS: Int = 3
        private const val CONCURRENT_IMPORT_FILES: Int = 5
    }

    fun importPages(tasks: List<ImportTask>,
                    taskFinishCallback: (ImportTask, List<Page>) -> Unit): Unit = runBlocking {
        val job = Job()

        tasks.chunked(CONCURRENT_IMPORT_TASKS).forEach { subTasks ->
            subTasks.map { importTask ->
                async(context = job) {
                    importTaskDAO.saveOrUpdate(importTask.run())
                    listSection(importTask)
                }
            }.forEach { importSectionResult ->
                val result: ImportSectionResult = importSectionResult.await()
                taskFinishCallback(result.task, result.pages)
            }
        }
    }

    fun importFiles(sectionName: String,
                    page: Page): List<ImportFileResult> = runBlocking {
        val job = Job()

        page.items.chunked(CONCURRENT_IMPORT_FILES).flatMap { items ->
            items.map { sectionItem ->
                async(context = job) {
                    try {
                        ImportFileResult.success(
                            fileId = sectionItem.fileId,
                            sectionFile = boraClient.retrieve(sectionName, sectionItem.fileId)
                        )
                    } catch (cause: Throwable) {
                        ImportFileResult.error(
                            fileId = sectionItem.fileId,
                            error = cause.message ?: "Unknown error"
                        )
                    }
                }
            }.map { result ->
                result.await()
            }
        }
    }

    private fun listSection(task: ImportTask): ImportSectionResult {

        var request: ListRequest = ListRequest.create(
            sectionName = task.sectionName,
            offset = 1,
            itemsPerPage = task.itemsPerPage,
            date = task.date
        )
        val pages: MutableList<Page> = mutableListOf()

        do {
            val page: Page = boraClient.list(request)

            if (!page.items.isEmpty()) {
                pages.add(page)
            }

            request = request.next(page.sessionId)
        } while (!page.items.isEmpty())

        return ImportSectionResult(task, pages.toList())
    }
}