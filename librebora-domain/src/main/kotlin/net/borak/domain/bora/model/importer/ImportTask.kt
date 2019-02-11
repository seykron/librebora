package net.borak.domain.bora.model.importer

import org.joda.time.DateTime
import java.util.*

data class ImportTask(val id: UUID,
                      val sectionName: String,
                      val date: DateTime,
                      val itemsPerPage: Int,
                      val metrics: ImportTaskMetrics = ImportTaskMetrics.empty(),
                      val status: ImportStatus = ImportStatus.WAITING,
                      val filesInError: List<String> = emptyList()) {

    companion object {
        fun new(sectionName: String,
                date: DateTime,
                itemsPerPage: Int): ImportTask {

            return ImportTask(
                id = UUID.randomUUID(),
                sectionName = sectionName,
                date = date.withTimeAtStartOfDay(),
                itemsPerPage = itemsPerPage
            )
        }
    }

    fun run(): ImportTask {
        return copy(
            status = ImportStatus.RUNNING
        )
    }

    fun terminate(metrics: ImportTaskMetrics,
                  filesInError: List<String>): ImportTask {
        return copy(
            metrics = metrics,
            filesInError = filesInError,
            status = ImportStatus.DONE
        )
    }

    fun terminate(): ImportTask {
        return copy(
            status = ImportStatus.DONE
        )
    }
}
