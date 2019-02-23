package net.borak.connector.bora.persistence

import net.borak.connector.bora.model.importer.ImportTask
import net.borak.support.persistence.TransactionSupport
import org.jetbrains.exposed.sql.deleteAll

class ImportTaskDAO : TransactionSupport() {

    fun saveOrUpdate(importTask: ImportTask): ImportTask = transaction {
        ImportTaskEntity.saveOrUpdate(importTask.id, importTask)
    }

    fun findActive(sectionName: String): List<ImportTask> = transaction {
        ImportTaskEntity.find {
            ImportTasks.sectionName eq sectionName
        }.sortedBy(ImportTaskEntity::date)
         .map(ImportTaskEntity::toDomainType)
    }

    fun deleteAll() = transaction {
        ImportTasks.deleteAll()
    }
}
