package net.borak.util.mock

import net.borak.service.bora.model.ImportStatus
import net.borak.service.bora.model.ImportTask
import org.joda.time.DateTime
import java.util.*

class TestImportTask(private val id: UUID = UUID.randomUUID(),
                     private val sectionName: String = "segunda",
                     private val date: DateTime = DateTime.now().withTimeAtStartOfDay(),
                     private val itemsPerPage: Int = 500,
                     private val status: ImportStatus = ImportStatus.WAITING) {

    fun new(): ImportTask {
        return ImportTask(
            id = id,
            sectionName = sectionName,
            date = date,
            itemsPerPage = itemsPerPage,
            status = status
        )
    }
}