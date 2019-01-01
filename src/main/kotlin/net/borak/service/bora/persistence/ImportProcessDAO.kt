package net.borak.service.bora.persistence

import net.borak.domain.persistence.TransactionSupport
import net.borak.service.bora.model.ImportProcess

class ImportProcessDAO : TransactionSupport() {

    fun save(importProcess: ImportProcess): ImportProcess = transaction {
        ImportProcessEntity.new(importProcess.id) {
            sectionName = importProcess.sectionName
            dayStart = importProcess.dayStart
            dayEnd = importProcess.dayEnd
            startDate = importProcess.startDate
        }.toImportProcess()
    }

    fun list(limit: Int,
             offset: Int = 0): List<ImportProcess> = transaction {

        ImportProcessEntity.all()
            .limit(limit, offset)
            .map(ImportProcessEntity::toImportProcess)
    }
}
