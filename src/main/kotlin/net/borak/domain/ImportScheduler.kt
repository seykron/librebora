package net.borak.domain

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class ImportScheduler(private val importService: ImportService) {

    fun start() = GlobalScope.launch {
        importService.import(
            sectionName =  "segunda",
            startDate = DateTime.now().minusDays(4),
            endDate = DateTime.now().minusDays(3)
        )
    }
}