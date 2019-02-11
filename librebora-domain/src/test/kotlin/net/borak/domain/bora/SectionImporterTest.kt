package net.borak.domain.bora

import net.borak.domain.bora.model.importer.ImportFileResult
import net.borak.domain.bora.model.importer.ImportTask
import net.borak.domain.bora.model.sections.SectionFile
import net.borak.domain.bora.model.sections.SectionListItem
import net.borak.util.mock.*
import org.junit.Test

class SectionImporterTest {

    companion object {
        private const val FILE_ID: String = "A1234B"
    }

    private val boraClient: TestBoraClient = TestBoraClient()
    private val importTaskDAO: TestImportTaskDAO = TestImportTaskDAO()

    @Test
    fun importPages() {
        val page = TestSectionPage(items = listOf(TestSectionListItem().new())).new()
        val importer = SectionImporter(
            boraClient = boraClient
                .list(page, page, TestSectionPage().new())
                .instance,
            importTaskDAO = importTaskDAO.instance
        )
        val task: ImportTask = TestImportTask(
            sectionName = "segunda"
        ).new()
        importer.importPages(listOf(task)) { task, results ->
            assert(results.size == 2)
            assert(results[0] == page)
            assert(results[1] == page)
        }
        boraClient.verifyAll()
    }

    @Test
    fun importFiles() {
        val sectionFile: SectionFile = TestSectionFile(id = FILE_ID).new()
        val item: SectionListItem = TestSectionListItem(fileId = FILE_ID).new()
        val importer = SectionImporter(
            boraClient = boraClient
                .retrieve("segunda", FILE_ID, sectionFile)
                .instance,
            importTaskDAO = importTaskDAO.instance
        )

        val results: List<ImportFileResult> = importer.importFiles(
            sectionName = "segunda",
            page = TestSectionPage(items = listOf(item)).new()
        )
        assert(results.size == 1)
        assert(results[0].sectionFile == sectionFile)
    }
}
