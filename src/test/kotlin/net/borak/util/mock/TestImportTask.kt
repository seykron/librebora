package net.borak.util.mock

import net.borak.service.bora.model.ImportTask
import org.joda.time.DateTime
import java.util.*

class TestImportTask(private val id: UUID = UUID.randomUUID(),
                     private val sectionName: String = "segunda",
                     private val startDate: DateTime = DateTime.now(),
                     private val endDate: DateTime = DateTime.now().plusDays(2),
                     private val dayStart: Int = 1,
                     private val dayEnd: Int = 10) {

    fun new(): ImportTask {
        return ImportTask(
            id = id,
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            dayStart = dayStart,
            dayEnd = dayEnd
        )
    }
}