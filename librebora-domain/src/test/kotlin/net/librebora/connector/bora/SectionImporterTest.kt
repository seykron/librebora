package net.librebora.connector.bora

import net.librebora.connector.bora.model.importer.ImportSectionTask
import net.librebora.connector.bora.model.importer.ImportTaskMetrics
import net.librebora.util.mock.*
import org.junit.Test
import java.lang.RuntimeException

class SectionImporterTest {

    companion object {
        private const val SESSION_ID: String = "session-id"
        private const val SECTION_NAME: String = "segunda"
    }

    private val boraClient: TestBoraClient = TestBoraClient()
    private val filesDAO: TestFilesDAO = TestFilesDAO()
    private val importTaskDAO: TestImportTaskDAO = TestImportTaskDAO()

    @Test
    fun importSection() {
        val page1 = TestSectionPage(
            sessionId = SESSION_ID,
            items = listOf(
                TestSectionListItem(fileId = "file-01").new(),
                TestSectionListItem(fileId = "file-02").new()
            )
        ).new()
        val page2 = TestSectionPage(
            sessionId = SESSION_ID,
            items = listOf(
                TestSectionListItem(fileId = "file-03").new()
            )
        ).new()
        val task: ImportSectionTask = TestImportTask(
            sectionName = SECTION_NAME
        ).new()
        val expectedTask = task.terminate(
            metrics = ImportTaskMetrics(
                numberOfPages = 3,
                numberOfFiles = 3
            ),
            filesInError = listOf("file-03")
        )

        val file01 = TestSectionFile(id = "file-01").new()
        val importer = SectionImporter(
            boraClient = boraClient
                .list(page1, page2, TestSectionPage().new())
                .retrieve(SECTION_NAME, "file-01", file01)
                .instance
        )
        val resultTask = importer.importSection(
            importTask = task,
            fileExists = { fileId ->
                if (fileId == "file-03") {
                    throw RuntimeException("File in error")
                }
                fileId == "file-02"
            }
        ) { sectionFile ->
            assert(sectionFile == file01)
        }

        assert(resultTask == expectedTask)

        boraClient.verifyAll()
        filesDAO.verifyAll()
        importTaskDAO.verifyAll()
    }
}
