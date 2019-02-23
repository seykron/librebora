package net.borak.domain

import net.borak.connector.bora.model.importer.ImportSectionTask
import net.borak.connector.bora.model.sections.SectionFile
import net.borak.domain.model.File
import net.borak.util.mock.*
import org.joda.time.DateTime
import org.junit.Test

class ImportServiceTest {

    private val sectionImporter: TestSectionImporter = TestSectionImporter()
    private val importTaskDAO: TestImportTaskDAO = TestImportTaskDAO()
    private val filesDAO: TestFilesDAO = TestFilesDAO()
    private val sectionName: String = "segunda"
    private val startDate: DateTime = DateTime.now().withTimeAtStartOfDay()
    private val endDate: DateTime = DateTime.now().plusDays(1).withTimeAtStartOfDay()

    @Test
    fun import_createProcessesAndFiles() {
        val task: ImportSectionTask = TestImportTask(
            sectionName = sectionName,
            date = startDate
        ).new()
        val sectionFile: SectionFile = TestSectionFile().new()
        val newFile: File = TestFile().new()

        val importService = ImportService(
            sectionImporter = sectionImporter
                .importSection(listOf(sectionFile, sectionFile))
                .instance,
            importSectionTaskDAO = importTaskDAO
                .findActive(
                    sectionName = sectionName,
                    result = listOf()
                )
                .saveOrUpdate(task)
                .instance,
            filesDAO = filesDAO
                .findFile(sectionFile.id, null)
                .saveOrUpdate(newFile) { savedFiles ->
                    assert(
                        newFile == savedFiles[0].copy(
                            id = newFile.id,
                            publicationDate = newFile.publicationDate
                        )
                    )
                }
                .instance
        )

        importService.import(sectionName, startDate, endDate)

        importTaskDAO.verifyAll()
        sectionImporter.verifyAll()
        filesDAO.verifyAll()
    }
}
