package net.borak.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.borak.domain.files.ImportService
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class ImportCommand(private val importService: ImportService) : CliktCommand(
    help = "Imports data from the BORA into the local database",
    name = "import"
) {

    companion object {
        private val DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC()
    }

    private val section by option("-s", "--section").required()
    private val fromDate by option("-f", "--from").required()
    private val toDate by option("-t", "--to").required()

    override fun run() {
        importService.import(
            sectionName = section,
            startDate = DateTime.parse(fromDate, DATE_FORMAT),
            endDate = DateTime.parse(toDate, DATE_FORMAT)
        )
    }
}