package net.borak.domain.bora

import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.borak.domain.bora.model.ImportTask
import net.borak.domain.bora.model.SectionFile
import net.borak.domain.bora.model.SectionListRequest
import net.borak.domain.bora.model.SectionPage
import net.borak.domain.bora.persistence.ImportTaskDAO

class SectionImporter(private val boraClient: BoraClient,
                      private val importTaskDAO: ImportTaskDAO) {

    private data class ImportSectionResult(val task: ImportTask,
                                           val pages: List<SectionPage>)

    companion object {
        private const val CONCURRENT_IMPORT_TASKS: Int = 3
        private const val CONCURRENT_IMPORT_FILES: Int = 5
    }

    fun importPages(tasks: List<ImportTask>,
                    taskFinishCallback: (ImportTask, List<SectionPage>) -> Unit): Unit = runBlocking {
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
                    sectionPage: SectionPage): List<SectionFile> = runBlocking {
        val job = Job()

        sectionPage.items.chunked(CONCURRENT_IMPORT_FILES).flatMap { items ->
            items.map { sectionItem ->
                async(context = job) {
                    boraClient.retrieve(sectionName, sectionItem.fileId)
                }
            }.map { result ->
                result.await()
            }
        }
    }

    private fun listSection(task: ImportTask): ImportSectionResult {

        var request: SectionListRequest = SectionListRequest.create(
            sectionName = task.sectionName,
            offset = 1,
            itemsPerPage = task.itemsPerPage,
            date = task.date
        )
        val sectionPages: MutableList<SectionPage> = mutableListOf()

        do {
            val sectionPage: SectionPage = boraClient.list(request)

            if (!sectionPage.items.isEmpty()) {
                sectionPages.add(sectionPage)
            }

            request = request.next(sectionPage.sessionId)
        } while (!sectionPage.items.isEmpty())

        return ImportSectionResult(task, sectionPages.toList())
    }
}