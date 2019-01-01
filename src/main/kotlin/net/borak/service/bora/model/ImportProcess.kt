package net.borak.service.bora.model

import org.joda.time.DateTime
import java.util.*

data class ImportProcess(val id: UUID,
                         val sectionName: String,
                         val dayStart: Int,
                         val dayEnd: Int,
                         val startDate: DateTime) {

    companion object {
        fun new(sectionName: String,
                dayStart: Int,
                dayEnd: Int,
                startDate: DateTime): ImportProcess {

            return ImportProcess(
                id = UUID.randomUUID(),
                sectionName = sectionName,
                dayStart = dayStart,
                dayEnd = dayEnd,
                startDate = startDate
            )
        }
    }
}
