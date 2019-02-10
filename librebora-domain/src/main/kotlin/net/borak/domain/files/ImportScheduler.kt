package net.borak.domain.files

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImportScheduler(private val importService: ImportService) {

    fun start() = GlobalScope.launch {
        /*importService.import(
            sectionName =  "segunda",
            startDate = DateTime.now().minusDays(4),
            endDate = DateTime.now().minusDays(3)
        )*/
    }
}