package net.borak.connector.bora.model.importer

/** Holds import task metrics.
 * @property numberOfPages Number of pages read.
 * @property numberOfFiles Total number of files imported.
 */
data class ImportTaskMetrics(
    val numberOfPages: Int,
    val numberOfFiles: Int
) {
    companion object {
        fun empty(): ImportTaskMetrics {
            return ImportTaskMetrics(
                    numberOfPages = 0,
                    numberOfFiles = 0
            )
        }
    }
}
