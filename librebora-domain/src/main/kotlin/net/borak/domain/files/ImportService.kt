package net.borak.domain.files

import net.borak.domain.bora.SectionImporter
import net.borak.domain.bora.model.importer.ImportFileResult
import net.borak.domain.bora.model.importer.ImportStatus
import net.borak.domain.bora.model.importer.ImportTask
import net.borak.domain.bora.model.importer.ImportTaskMetrics
import net.borak.domain.bora.model.sections.Page
import net.borak.domain.bora.model.sections.SectionFile
import net.borak.domain.bora.persistence.ImportTaskDAO
import net.borak.domain.files.model.File
import net.borak.domain.files.model.Section
import net.borak.domain.files.persistence.FilesDAO
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
                                   pages: List<Page>) {

        val importFilesResults: List<ImportFileResult> = pages.flatMap { sectionPage ->
            sectionImporter.importFiles(importTask.sectionName, sectionPage)
        }
        val importedFiles: List<SectionFile> = importFilesResults.filter { importFileResult ->
            importFileResult.isSuccess()
        }.map { importFileResult ->
            importFileResult.sectionFile ?: throw RuntimeException("Section file cannot be resolved")
        }
        val filesInError: List<String> = importFilesResults.filter { importFileResult ->
            importFileResult.isError()
        }.map { importFileResult ->
            importFileResult.fileId
        }

        importedFiles.forEach { sectionFile ->
            saveOrUpdateFile(importTask.sectionName, sectionFile)
        }

        importTaskDAO.saveOrUpdate(importTask.terminate(
            filesInError = filesInError,
            metrics = ImportTaskMetrics(
                numberOfPages = pages.size,
                numberOfFiles = pages.fold(0) { count, page ->
                    count + page.items.size
                }
            )
        ))
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

    private fun saveOrUpdateFile(sectionName: String,
                                 sectionFile: SectionFile): File {

        return filesDAO.find(sectionFile.id) ?: run {
            filesDAO.saveOrUpdate(File.create(
                fileId = sectionFile.id,
                section = Section.fromName(sectionName),
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