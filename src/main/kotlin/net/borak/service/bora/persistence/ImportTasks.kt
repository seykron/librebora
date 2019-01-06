package net.borak.service.bora.persistence

import net.borak.service.bora.model.ImportTask
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object ImportTasks : UUIDTable(name = "import_tasks") {
    val sectionName = varchar("section_name", 30)
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val dayStart = integer("day_start")
    val dayEnd = integer("day_end")
}

class ImportTaskEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ImportTaskEntity>(ImportTasks)
    var sectionName: String by ImportTasks.sectionName
    var startDate: DateTime by ImportTasks.startDate
    var endDate: DateTime by ImportTasks.endDate
    var dayStart: Int by ImportTasks.dayStart
    var dayEnd: Int by ImportTasks.dayEnd

    fun toImportTask(): ImportTask {
        return ImportTask(
            id = id.value,
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            dayStart = dayStart,
            dayEnd = dayEnd
        )
    }
}
