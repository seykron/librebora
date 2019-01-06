package net.borak.service.bora.persistence

import net.borak.domain.persistence.TransactionSupport
import net.borak.service.bora.model.ImportProcess
import org.jetbrains.exposed.sql.deleteWhere
import org.joda.time.DateTime

class ImportProcessDAO : TransactionSupport() {

    fun save(importProcess: ImportProcess): ImportProcess = transaction {
        ImportProcessEntity.new(importProcess.id) {
            sectionName = importProcess.sectionName
            startDate = importProcess.startDate.withTimeAtStartOfDay()
            endDate = importProcess.endDate.withTimeAtStartOfDay()
            dayStart = importProcess.dayStart
            dayEnd = importProcess.dayEnd
        }.toImportProcess()
    }

    fun find(sectionName: String,
             startDate: DateTime,
             endDate: DateTime,
             limit: Int,
             offset: Int = 0): List<ImportProcess> = transaction {

        ImportProcessEntity.find {
            ImportProcesses.sectionName eq sectionName
            ImportProcesses.startDate eq startDate.withTimeAtStartOfDay()
            ImportProcesses.endDate eq endDate.withTimeAtStartOfDay()
        }.limit(limit, offset)
         .map(ImportProcessEntity::toImportProcess)
    }

    fun delete(importProcess: ImportProcess) {
        ImportProcesses.deleteWhere {
            ImportProcesses.id eq importProcess.id
        }
    }
}
