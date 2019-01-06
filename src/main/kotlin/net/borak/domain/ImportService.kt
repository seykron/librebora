package net.borak.domain

import net.borak.domain.model.File
import net.borak.domain.persistence.FilesDAO
import net.borak.service.bora.SectionImporter
import net.borak.service.bora.model.ImportProcess
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.persistence.ImportProcessDAO
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ImportService(private val sectionImporter: SectionImporter,
                    private val importProcessDAO: ImportProcessDAO,
                    private val filesDAO: FilesDAO) {

    companion object {
        private const val DAYS_BATCH_SIZE: Int = 4
        private val DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    }

    fun import(sectionName: String,
               startDate: DateTime,
               endDate: DateTime) {

        val importProcesses: List<ImportProcess> = resolveImportProcesses(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate
        )

        sectionImporter.importPages(importProcesses) { importProcess, sectionPages ->
            val sectionFiles: List<SectionFile> = sectionPages.flatMap { sectionPage ->
                sectionImporter.importFiles(sectionName, sectionPage)
            }
            sectionFiles.forEach { sectionFile ->
                saveOrUpdateFile(sectionFile)
            }
            importProcessDAO.delete(importProcess)
        }
    }

    private fun resolveImportProcesses(sectionName: String,
                                       startDate: DateTime,
                                       endDate: DateTime): List<ImportProcess> {
        return importProcessDAO.find(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            limit = 100
        ).let { results ->
            if (results.isEmpty()) {
                createImportProcesses(
                    sectionName = sectionName,
                    startDate = startDate,
                    endDate = endDate
                ).map(importProcessDAO::save)
            } else {
                results
            }
        }
    }

    private fun createImportProcesses(sectionName: String,
                                      startDate: DateTime,
                                      endDate: DateTime): List<ImportProcess> {

        val days: Int = Duration(startDate, endDate).standardDays.toInt()

        return (0..days step DAYS_BATCH_SIZE).map { dayStart ->
            val dayEnd: Int = if (dayStart + DAYS_BATCH_SIZE < days) {
                dayStart + DAYS_BATCH_SIZE
            } else {
                days % DAYS_BATCH_SIZE
            }

            ImportProcess.new(
                sectionName = sectionName,
                startDate = startDate,
                endDate = endDate,
                dayStart = dayStart,
                dayEnd = dayEnd
            )
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