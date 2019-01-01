package net.borak.service.bora

import net.borak.domain.model.File
import net.borak.domain.model.FileNotFoundException
import net.borak.domain.persistence.FilesDAO
import net.borak.service.bora.model.SectionFile
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class BoraService(private val boraClient: BoraClient,
                  private val filesDAO: FilesDAO) {

    companion object {
        private val DATE_TIME_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("yyyyMMdd")
    }

    fun findFile(fileId: String): File {
        return filesDAO.find(fileId) ?:
            throw FileNotFoundException("File with id $fileId not found")
    }

    fun importFile(sectionName: String,
                   fileId: String): File {

        return filesDAO.find(fileId) ?: run {
            val sectionFile: SectionFile = boraClient.retrieve(
                sectionName = sectionName,
                fileId = fileId
            )
            filesDAO.saveOrUpdate(File.create(
                fileId = sectionFile.id,
                categoryId = sectionFile.categoryId,
                categoryName = sectionFile.category,
                text = sectionFile.text,
                publicationDate = DateTime.parse(sectionFile.publicationDate, DATE_TIME_FORMAT),
                pdfFile = sectionFile.pdfFile
            ))
        }
    }
}
