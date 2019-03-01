package net.librebora.domain.persistence

import net.librebora.domain.model.Cursor
import net.librebora.domain.model.File
import net.librebora.domain.model.Section
import net.librebora.support.persistence.TransactionSupport

class FilesDAO : TransactionSupport() {

    fun find(fileId: String): File? = transaction {
        val files: List<File> = FileEntity.find {
            Files.fileId eq fileId
        }.map(FileEntity::toDomainType)

        if (files.isEmpty()) {
            null
        } else {
            files[0]
        }
    }

    fun list(section: Section,
             cursor: Cursor): List<File> = transaction {
        FileEntity.find {
            Files.section eq section
        }.limit(
            n = cursor.itemsPerPage,
            offset = cursor.pageNumber
        ).map(FileEntity::toDomainType)
    }

    fun listByCategory(section: Section,
                       categoryName: String): List<File> = transaction {
        FileEntity.find {
            Files.section eq section
            Files.categoryName eq categoryName
        }.map(FileEntity::toDomainType)
    }

    fun saveOrUpdate(file: File): File = transaction {
        FileEntity.saveOrUpdate(file.id, file)
    }
}
