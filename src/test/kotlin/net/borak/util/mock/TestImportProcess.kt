package net.borak.util.mock

import net.borak.service.bora.model.ImportProcess
import org.joda.time.DateTime
import java.util.*

class TestImportProcess(private val id: UUID = UUID.randomUUID(),
                        private val sectionName: String = "segunda",
                        private val dayStart: Int = 1,
                        private val dayEnd: Int = 10,
                        private val startDate: DateTime = DateTime.now()) {

    fun new(): ImportProcess {
        return ImportProcess(
            id = id,
            sectionName = sectionName,
            dayStart = dayStart,
            dayEnd = dayEnd,
            startDate = startDate
        )
    }
}