package net.borak.connector.bora.nlp.parser

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class FileInfoParser {

    companion object {
        private val FILE_INFO_PARSER: Regex = Regex("e. (\\d{2}/\\d{2}/\\d{4}) N° (\\d+/\\d{2})")
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy").withZoneUTC()
    }

    fun parse(fileInfo: String): FileInfo {
        return FILE_INFO_PARSER.find(fileInfo)?.let { matchResult ->
            FileInfo(
                creationDate = DateTime.parse(matchResult.groupValues[1], DATE_FORMAT),
                fileId = matchResult.groupValues[2]
            )
        } ?: throw ParseException("Cannot parse file info")
    }
}
