package net.borak.domain.bora.model.importer

data class ImportTaskMetrics(val numberOfPages: Int,
                             val numberOfFiles: Int) {
    companion object {
        fun empty(): ImportTaskMetrics {
            return ImportTaskMetrics(
                    numberOfPages = 0,
                    numberOfFiles = 0
            )
        }
    }
}
