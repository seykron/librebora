package net.borak.service.bora.persistence

import net.borak.service.bora.model.ImportProcess
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object ImportProcesses : UUIDTable(name = "import_processes") {
    val sectionName = varchar("section_name", 30)
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val dayStart = integer("day_start")
    val dayEnd = integer("day_end")
}

class ImportProcessEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ImportProcessEntity>(ImportProcesses)
    var sectionName: String by ImportProcesses.sectionName
    var startDate: DateTime by ImportProcesses.startDate
    var endDate: DateTime by ImportProcesses.endDate
    var dayStart: Int by ImportProcesses.dayStart
    var dayEnd: Int by ImportProcesses.dayEnd

    fun toImportProcess(): ImportProcess {
        return ImportProcess(
            id = id.value,
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            dayStart = dayStart,
            dayEnd = dayEnd
        )
    }
}
