package net.borak.domain

import net.borak.domain.model.File
import net.borak.service.bora.model.ImportProcess
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionPage
import net.borak.util.mock.*
import org.joda.time.DateTime
import org.junit.Test

class ImportServiceTest {

    private val sectionImporter: TestSectionImporter = TestSectionImporter()
    private val importProcessDAO: TestImportProcessDAO = TestImportProcessDAO()
    private val filesDAO: TestFilesDAO = TestFilesDAO()
    private val sectionName: String = "segunda"
    private val startDate: DateTime = DateTime.now().withTimeAtStartOfDay()
    private val endDate: DateTime = DateTime.now().plusDays(1).withTimeAtStartOfDay()

    @Test
    fun import_createProcessesAndFiles() {
        val process: ImportProcess = TestImportProcess(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            dayStart = 0,
            dayEnd = 1
        ).new()
        val sectionPage: SectionPage = TestSectionPage().new()
        val sectionFile: SectionFile = TestSectionFile().new()
        val newFile: File = TestFile().new()

        val importService = ImportService(
            sectionImporter = sectionImporter
                .importPages { results ->
                    results.callback(process, listOf(sectionPage))
                    assert(listOf(process).containsAll(results.processes))
                }
                .importFiles(
                    sectionName = sectionName,
                    sectionPage = sectionPage,
                    results = listOf(sectionFile)
                )
                .instance,
            importProcessDAO = importProcessDAO
                .find(
                    sectionName = sectionName,
                    startDate = startDate,
                    endDate = endDate,
                    limit = 100,
                    result = listOf()
                )
                .save(process) { savedProcess ->
                    assert(process == savedProcess.copy(id = process.id))
                }
                .delete(process)
                .instance,
            filesDAO = filesDAO
                .findFile(sectionFile.id, null)
                .saveOrUpdate(newFile) { savedFile ->
                    assert(
                        newFile == savedFile.copy(
                            id = newFile.id,
                            publicationDate = newFile.publicationDate
                        )
                    )
                }
                .instance
        )

        importService.import(sectionName, startDate, endDate)

        sectionImporter.verifyAll()
        importProcessDAO.verifyAll()
        filesDAO.verifyAll()
    }

    @Test
    fun import_resumeAndUpdate() {
        val processes: List<ImportProcess> = listOf(
            TestImportProcess(
                sectionName = sectionName,
                startDate = startDate,
                endDate = endDate
            ).new()
        )
        val sectionPage: SectionPage = TestSectionPage().new()
        val sectionFile: SectionFile = TestSectionFile().new()
        val existingFile: File = TestFile().new()

        val importService = ImportService(
            sectionImporter = sectionImporter
                .importPages { results ->
                    results.callback(processes[0], listOf(sectionPage))
                    assert(results.processes == processes)
                }
                .importFiles(
                    sectionName = sectionName,
                    sectionPage = sectionPage,
                    results = listOf(sectionFile)
                )
                .instance,
            importProcessDAO = importProcessDAO
                .find(
                    sectionName = sectionName,
                    startDate = startDate,
                    endDate = endDate,
                    limit = 100,
                    result = processes
                )
                .delete(processes[0])
                .instance,
            filesDAO = filesDAO
                .findFile(sectionFile.id, existingFile)
                .instance
        )

        importService.import(sectionName, startDate, endDate)

        sectionImporter.verifyAll()
        importProcessDAO.verifyAll()
        filesDAO.verifyAll()
    }
}