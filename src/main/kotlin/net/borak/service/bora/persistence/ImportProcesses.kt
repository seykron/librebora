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
    val dayStart = integer("day_start")
    val dayEnd = integer("day_end")
    val startDate = datetime("start_date")
}

class ImportProcessEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ImportProcessEntity>(ImportProcesses)
    var sectionName: String by ImportProcesses.sectionName
    var dayStart: Int by ImportProcesses.dayStart
    var dayEnd: Int by ImportProcesses.dayEnd
    var startDate: DateTime by ImportProcesses.startDate

    fun toImportProcess(): ImportProcess {
        return ImportProcess(
            id = id.value,
            sectionName = sectionName,
            dayStart = dayStart,
            dayEnd = dayEnd,
            startDate = startDate
        )
    }
}
