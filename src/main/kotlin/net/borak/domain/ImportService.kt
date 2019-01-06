package net.borak.domain

import net.borak.domain.model.File
import net.borak.domain.persistence.FilesDAO
import net.borak.service.bora.SectionImporter
import net.borak.service.bora.model.*
import net.borak.service.bora.persistence.ImportTaskDAO
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ImportService(private val sectionImporter: SectionImporter,
                    private val importTaskDAO: ImportTaskDAO,
                    private val filesDAO: FilesDAO) {

    companion object {
        private const val ITEMS_PER_PAGE: Int = 500
        private val DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    }

    fun import(sectionName: String,
               startDate: DateTime,
               endDate: DateTime) {

        val importTasks: List<ImportTask> = resolveImportTasks(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate
        )

        sectionImporter.importPages(importTasks, this::importPageCallback)
    }

    private fun importPageCallback(importTask: ImportTask,
                                   sectionPages: List<SectionPage>) {

        val sectionFiles: List<SectionFile> = sectionPages.flatMap { sectionPage ->
            sectionImporter.importFiles(importTask.sectionName, sectionPage)
        }
        sectionFiles.forEach { sectionFile ->
            saveOrUpdateFile(sectionFile)
        }

        importTaskDAO.saveOrUpdate(importTask.terminate(ImportTaskMetrics(
            numberOfPages = sectionPages.size,
            numberOfFiles = sectionPages.fold(0) { count, page ->
                count + page.items.size
            }
        )))
    }

    private fun resolveImportTasks(sectionName: String,
                                   startDate: DateTime,
                                   endDate: DateTime): List<ImportTask> {
        return importTaskDAO.findActive(
            sectionName = sectionName
        ).let { tasks ->
            createImportTasksIfRequired(
                sectionName = sectionName,
                startDate = startDate,
                endDate = endDate,
                tasks = tasks
            ).filter { importTask ->
                importTask.status != ImportStatus.DONE
            }
        }
    }

    private fun createImportTasksIfRequired(sectionName: String,
                                            startDate: DateTime,
                                            endDate: DateTime,
                                            tasks: List<ImportTask>): List<ImportTask> {

        val numberOfDays: Int = Duration(
            startDate.withTimeAtStartOfDay(),
            endDate.withTimeAtStartOfDay()
        ).standardDays.toInt()

        return (0..numberOfDays).map { day ->
            val date: DateTime = startDate.plusDays(day).withTimeAtStartOfDay()

            tasks.find { importTask ->
                importTask.date == date
            } ?: importTaskDAO.saveOrUpdate(ImportTask.new(
                sectionName = sectionName,
                date = startDate.plusDays(day),
                itemsPerPage = ITEMS_PER_PAGE
            ))
        }
    }

    private fun saveOrUpdateFile(sectionFile: SectionFile): File {
        return filesDAO.find(sectionFile.id) ?: run {
            filesDAO.saveOrUpdate(File.create(
                fileId = sectionFile.id,
                categoryId = sectionFile.categoryId,
                categoryName = sectionFile.category,
                text = sectionFile.text,
                publicationDate = DateTime.parse(sectionFile.publicationDate,
                    DATE_TIME_FORMAT
                ),
                pdfFile = sectionFile.pdfFile
            ))
        }
    }
}