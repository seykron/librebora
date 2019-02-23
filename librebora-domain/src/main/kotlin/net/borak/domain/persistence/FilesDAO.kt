package net.borak.domain.persistence

import net.borak.domain.model.File
import net.borak.domain.model.Section
import net.borak.support.persistence.TransactionSupport

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

    fun list(section: Section): List<File> = transaction {
        FileEntity.find {
            Files.section eq section
        }.map(FileEntity::toDomainType)
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
