package net.borak.service.bora

import net.borak.service.bora.model.ImportTask
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionListItem
import net.borak.service.bora.model.SectionPage
import net.borak.util.mock.TestBoraClient
import net.borak.util.mock.TestSectionFile
import net.borak.util.mock.TestSectionListItem
import net.borak.util.mock.TestSectionPage
import org.joda.time.DateTime
import org.junit.Test

class SectionImporterTest {

    companion object {
        private const val FILE_ID: String = "A1234B"
    }

    private val boraClient: TestBoraClient = TestBoraClient()

    @Test
    fun importPages() {
        val page = SectionPage("", listOf())
        val importer = SectionImporter(
            boraClient = boraClient
                .list(page)
                .instance
        )
        val task: ImportTask = ImportTask.new(
            sectionName = "segunda",
            startDate = DateTime.now(),
            endDate = DateTime.now().plusDays(2),
            dayStart = 0,
            dayEnd = 2
        )
        importer.importPages(listOf(task)) { task, results ->
            assert(results.size == 3)
            assert(results[0] == page)
            assert(results[1] == page)
            assert(results[2] == page)
        }
    }

    @Test
    fun importFiles() {
        val sectionFile: SectionFile = TestSectionFile(id = FILE_ID).new()
        val item: SectionListItem = TestSectionListItem(fileId = FILE_ID).new()
        val importer = SectionImporter(
            boraClient = boraClient
                .retrieve("segunda", FILE_ID, sectionFile)
                .instance
        )

        val results: List<SectionFile> = importer.importFiles(
            sectionName = "segunda",
            sectionPage = TestSectionPage(items = listOf(item)).new()
        )
        assert(results.size == 1)
        assert(results[0] == sectionFile)
    }
}
