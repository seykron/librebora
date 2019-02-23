package net.borak.connector.bora.persistence

import net.borak.connector.bora.model.importer.ImportSectionTask
import net.borak.support.persistence.TransactionSupport
import org.jetbrains.exposed.sql.deleteAll

class ImportSectionTaskDAO : TransactionSupport() {

    fun saveOrUpdate(importTask: ImportSectionTask): ImportSectionTask = transaction {
        ImportTaskEntity.saveOrUpdate(importTask.id, importTask)
    }

    fun findActive(sectionName: String): List<ImportSectionTask> = transaction {
        ImportTaskEntity.find {
            ImportTasks.sectionName eq sectionName
        }.sortedBy(ImportTaskEntity::date)
         .map(ImportTaskEntity::toDomainType)
    }

    fun deleteAll() = transaction {
        ImportTasks.deleteAll()
    }
}
