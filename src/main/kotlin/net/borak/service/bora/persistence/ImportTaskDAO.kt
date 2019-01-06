package net.borak.service.bora.persistence

import net.borak.domain.persistence.TransactionSupport
import net.borak.service.bora.model.ImportTask
import org.jetbrains.exposed.sql.deleteWhere
import org.joda.time.DateTime

class ImportTaskDAO : TransactionSupport() {

    fun save(importTask: ImportTask): ImportTask = transaction {
        ImportTaskEntity.new(importTask.id) {
            sectionName = importTask.sectionName
            startDate = importTask.startDate.withTimeAtStartOfDay()
            endDate = importTask.endDate.withTimeAtStartOfDay()
            dayStart = importTask.dayStart
            dayEnd = importTask.dayEnd
        }.toImportTask()
    }

    fun find(sectionName: String,
             startDate: DateTime,
             endDate: DateTime,
             limit: Int,
             offset: Int = 0): List<ImportTask> = transaction {

        ImportTaskEntity.find {
            ImportTasks.sectionName eq sectionName
            ImportTasks.startDate eq startDate.withTimeAtStartOfDay()
            ImportTasks.endDate eq endDate.withTimeAtStartOfDay()
        }.limit(limit, offset)
         .map(ImportTaskEntity::toImportTask)
    }

    fun delete(importTask: ImportTask) {
        ImportTasks.deleteWhere {
            ImportTasks.id eq importTask.id
        }
    }
}
