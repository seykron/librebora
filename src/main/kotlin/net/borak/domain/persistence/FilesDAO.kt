package net.borak.domain.persistence

import net.borak.domain.model.File
import net.borak.domain.model.Section
import org.jetbrains.exposed.exceptions.EntityNotFoundException

class FilesDAO : TransactionSupport() {

    fun find(fileId: String): File? = transaction {
        val files: List<File> = FileEntity.find {
            Files.fileId eq fileId
        }.map(FileEntity::toFile)

        if (files.isEmpty()) {
            null
        } else {
            files[0]
        }
    }

    fun list(section: Section): List<File> = transaction {
        FileEntity.find {
            Files.section eq section
        }.map(FileEntity::toFile)
    }

    fun saveOrUpdate(file: File): File = transaction {
        try {
            FileEntity[file.id].update(
                fileId = file.fileId,
                categoryId = file.categoryId,
                categoryName = file.categoryName,
                text = file.text,
                publicationDate = file.publicationDate,
                pdfFile = file.pdfFile
            )
        } catch (cause: EntityNotFoundException) {
            FileEntity.new(file.id) {
                fileId = file.fileId
                categoryId = file.categoryId
                categoryName = file.categoryName
                text = file.text
                publicationDate = file.publicationDate
                pdfFile = file.pdfFile
            }
        }.toFile()
    }
}
