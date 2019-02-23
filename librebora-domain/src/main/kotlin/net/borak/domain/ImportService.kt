package net.borak.domain

import net.borak.connector.bora.SectionImporter
import net.borak.connector.bora.model.importer.ImportStatus
import net.borak.connector.bora.model.importer.ImportSectionTask
import net.borak.connector.bora.model.sections.SectionFile
import net.borak.connector.bora.persistence.ImportSectionTaskDAO
import net.borak.domain.model.File
import net.borak.domain.model.Section
import net.borak.domain.persistence.FilesDAO
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ImportService(private val sectionImporter: SectionImporter,
                    private val importSectionTaskDAO: ImportSectionTaskDAO,
                    private val filesDAO: FilesDAO) {

    companion object {
        private const val ITEMS_PER_PAGE: Int = 500
        private val DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    }

    private val logger: Logger = LoggerFactory.getLogger(ImportService::class.java)

    fun import(
        sectionName: String,
        startDate: DateTime,
        endDate: DateTime
    ): List<ImportSectionTask> {
        return resolveImportTasks(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate
        ).map { importTask ->
            val updatedTask = sectionImporter.importSection(
                importTask = importTask,
                fileExists = this::fileExists,
                callback = saveNewFile(sectionName)
            )
            importSectionTaskDAO.saveOrUpdate(updatedTask)
        }
    }

    private fun resolveImportTasks(
        sectionName: String,
        startDate: DateTime,
        endDate: DateTime
    ): List<ImportSectionTask> {
        return importSectionTaskDAO.findActive(
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

    private fun createImportTasksIfRequired(
        sectionName: String,
        startDate: DateTime,
        endDate: DateTime,
        tasks: List<ImportSectionTask>
    ): List<ImportSectionTask> {

        val numberOfDays: Int = Duration(
            startDate.withTimeAtStartOfDay(),
            endDate.withTimeAtStartOfDay()
        ).standardDays.toInt()

        return (0..numberOfDays).map { day ->
            val date: DateTime = startDate.plusDays(day).withTimeAtStartOfDay()

            tasks.find { importTask ->
                importTask.date == date
            } ?: importSectionTaskDAO.saveOrUpdate(ImportSectionTask.new(
                sectionName = sectionName,
                date = startDate.plusDays(day),
                itemsPerPage = ITEMS_PER_PAGE
            ))
        }
    }

    private fun fileExists(fileId: String): Boolean {
        return filesDAO.find(fileId) != null
    }

    private fun saveNewFile(
        sectionName: String
    ): (SectionFile) -> Unit = { sectionFile: SectionFile ->
        logger.info("creating file ${sectionFile.id} from section $sectionFile")

        val createdFile = filesDAO.saveOrUpdate(File.create(
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

        logger.info("file successfully created: $createdFile")
    }
}