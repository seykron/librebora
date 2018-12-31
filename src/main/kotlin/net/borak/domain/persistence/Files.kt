package net.borak.domain.persistence

import net.borak.domain.model.File
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object Files : UUIDTable() {
    val fileId = varchar("file_id", 30).uniqueIndex()
    val categoryId = varchar("category_id", 50)
    val categoryName = varchar("category_name", 255)
    val text = text("full_text")
    val publicationDate = datetime("publication_date")
    val pdfFile = varchar("pdf_file", 255)
}

class FileEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<FileEntity>(Files)
    var fileId: String by Files.fileId
    var categoryId: String by Files.categoryId
    var categoryName: String by Files.categoryName
    var text: String by Files.text
    var publicationDate: DateTime by Files.publicationDate
    var pdfFile: String by Files.pdfFile

    fun update(fileId: String,
               categoryId: String,
               categoryName: String,
               text: String,
               publicationDate: DateTime,
               pdfFile: String): FileEntity {

        this.fileId = fileId
        this.categoryId = categoryId
        this.categoryName = categoryName
        this.text = text
        this.publicationDate = publicationDate
        this.pdfFile = pdfFile

        return this
    }

    fun toFile(): File {
        return File(
            id = id.value,
            fileId = fileId,
            categoryId = categoryId,
            categoryName = categoryName,
            text = text,
            publicationDate = publicationDate,
            pdfFile = pdfFile
        )
    }
}
