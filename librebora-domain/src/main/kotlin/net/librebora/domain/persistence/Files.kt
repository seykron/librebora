package net.librebora.domain.persistence

import net.librebora.domain.model.File
import net.librebora.domain.model.Section
import net.librebora.support.persistence.AbstractEntity
import net.librebora.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object Files : UUIDTable(name = "files") {
    const val FILE_ID_LENGTH: Int = 30

    val fileId = varchar("file_id", FILE_ID_LENGTH).uniqueIndex()
    val section = enumeration("section", Section::class)
    val categoryId = varchar("category_id", 50)
    val categoryName = varchar("category_name", 255)
    val text = text("full_text")
    val publicationDate = datetime("publication_date")
    val pdfFile = varchar("pdf_file", 255)
}

class FileEntity(id: EntityID<UUID>) : AbstractEntity<File>(id) {
    companion object : AbstractEntityClass<File, FileEntity>(Files)
    var fileId: String by Files.fileId
    var section: Section by Files.section
    var categoryId: String by Files.categoryId
    var categoryName: String by Files.categoryName
    var text: String by Files.text
    var publicationDate: DateTime by Files.publicationDate
    var pdfFile: String by Files.pdfFile

    override fun create(source: File): AbstractEntity<File> {
        this.section = source.section
        this.fileId = source.fileId
        this.categoryId = source.categoryId
        this.categoryName = source.categoryName
        this.text = source.text
        this.publicationDate = source.publicationDate
        this.pdfFile = source.pdfFile
        return this
    }

    override fun update(source: File): FileEntity {
        return this
    }

    override fun toDomainType(): File {
        return File(
            id = id.value,
            fileId = fileId,
            section = section,
            categoryId = categoryId,
            categoryName = categoryName,
            text = text,
            publicationDate = publicationDate,
            pdfFile = pdfFile
        )
    }
}
