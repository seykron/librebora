package net.borak.connector.bora.model.importer

import net.borak.domain.model.Section
import org.joda.time.DateTime
import java.util.*

/** Represents a scheduled task to import data from a BORA [Section].
 *
 * It fails only if the pagination fails. If a file import fails, it is added
 * to the list of failed files in the task.
 *
 * @property id Id of the scheduled task.
 * @property sectionName Name of the section to import files from.
 * @property date Date to import.
 * @property itemsPerPage Amount of items per page in the specified date, used for pagination.
 * @property metrics Saves metrics once the import process finished.
 * @property status Current scheduled task status.
 * @property errorMessage If status is [ImportStatus.ERROR], it contains the related error message.
 * @property filesInError List of files ids that couldn't be retrieved from the BORA.
 */
data class ImportSectionTask(
    val id: UUID,
    val sectionName: String,
    val date: DateTime,
    val itemsPerPage: Int,
    val metrics: ImportTaskMetrics = ImportTaskMetrics.empty(),
    val status: ImportStatus = ImportStatus.WAITING,
    val errorMessage: String?,
    val filesInError: List<String> = emptyList()
) {

    companion object {

        /** Creates a new scheduled task in [ImportStatus.WAITING] state.
         * @param sectionName Section to import.
         * @param date Date to import.
         * @param itemsPerPage Items per page, used for pagination.
         */
        fun new(
            sectionName: String,
            date: DateTime,
            itemsPerPage: Int
        ): ImportSectionTask {

            return ImportSectionTask(
                id = UUID.randomUUID(),
                sectionName = sectionName,
                date = date.withTimeAtStartOfDay(),
                itemsPerPage = itemsPerPage,
                errorMessage = null
            )
        }
    }

    /** Marks this schedule task for running.
     */
    fun run(): ImportSectionTask {
        return copy(
            status = ImportStatus.RUNNING
        )
    }

    /** Terminates the import task and sets the summary information.
     * @param metrics Import metrics.
     * @param filesInError List of files in error.
     */
    fun terminate(
        metrics: ImportTaskMetrics,
        filesInError: List<String>
    ): ImportSectionTask {
        return copy(
            metrics = metrics,
            filesInError = filesInError,
            status = ImportStatus.DONE
        )
    }

    fun terminate(): ImportSectionTask {
        return copy(
            status = ImportStatus.DONE
        )
    }

    /** Terminates this scheduled task with an error.
     * @param message Error message.
     */
    fun error(message: String): ImportSectionTask {
        return copy(
            status = ImportStatus.ERROR,
            errorMessage = message
        )
    }

    fun timestamp(): String {
        return date.toString("YYYY-MM-dd")
    }
}
