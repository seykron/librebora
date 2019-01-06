package net.borak.service.bora.model

import org.joda.time.DateTime
import java.util.*

data class ImportProcess(val id: UUID,
                         val sectionName: String,
                         val startDate: DateTime,
                         val endDate: DateTime,
                         val dayStart: Int,
                         val dayEnd: Int) {

    companion object {
        fun new(sectionName: String,
                startDate: DateTime,
                endDate: DateTime,
                dayStart: Int,
                dayEnd: Int): ImportProcess {

            return ImportProcess(
                id = UUID.randomUUID(),
                sectionName = sectionName,
                startDate = startDate,
                endDate = endDate,
                dayStart = dayStart,
                dayEnd = dayEnd
            )
        }
    }
}
