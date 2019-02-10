package net.borak.support.csv

data class ParsingContext(val currentColumn: Int,
                          val withinField: Boolean,
                          val bufferSize: Int,
                          val escape: Boolean,
                          val fieldStartIndex: Int,
                          val record: MutableList<ByteArray>) {
    companion object {
        fun new(): ParsingContext {
            return ParsingContext(
                currentColumn = 0,
                withinField = true,
                bufferSize = 0,
                escape = false,
                fieldStartIndex = 0,
                record = mutableListOf()
            )
        }
    }
}
