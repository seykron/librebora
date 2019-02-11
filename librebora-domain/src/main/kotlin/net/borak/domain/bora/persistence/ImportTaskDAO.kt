package net.borak.domain.bora.persistence

import net.borak.domain.bora.model.importer.ImportTask
import net.borak.persistence.TransactionSupport
import org.jetbrains.exposed.exceptions.EntityNotFoundException

class ImportTaskDAO : TransactionSupport() {

    fun saveOrUpdate(importTask: ImportTask): ImportTask = transaction {
        try {
            ImportTaskEntity[importTask.id].update(
                numberOfPages = importTask.metrics.numberOfPages,
                numberOfFiles = importTask.metrics.numberOfFiles,
                status = importTask.status
            ).toImportTask()
        } catch (cause: EntityNotFoundException) {
            ImportTaskEntity.new(importTask.id) {
                sectionName = importTask.sectionName
                date = importTask.date.withTimeAtStartOfDay()
                itemsPerPage = importTask.itemsPerPage
                numberOfPages = importTask.metrics.numberOfPages
                numberOfFiles = importTask.metrics.numberOfFiles
                status = importTask.status
            }.toImportTask()
        }
    }

    fun findActive(sectionName: String): List<ImportTask> = transaction {
        ImportTaskEntity.find {
            ImportTasks.sectionName eq sectionName
        }.sortedBy(ImportTaskEntity::date)
         .map(ImportTaskEntity::toImportTask)
    }
}
