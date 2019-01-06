package net.borak.domain

import net.borak.domain.model.File
import net.borak.service.bora.model.ImportTask
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionPage
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
        val task: ImportTask = TestImportTask(
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
                    results.callback(task, listOf(sectionPage))
                    assert(listOf(task).containsAll(results.tasks))
                }
                .importFiles(
                    sectionName = sectionName,
                    sectionPage = sectionPage,
                    results = listOf(sectionFile)
                )
                .instance,
            importTaskDAO = importTaskDAO
                .find(
                    sectionName = sectionName,
                    startDate = startDate,
                    endDate = endDate,
                    limit = 100,
                    result = listOf()
                )
                .save(task) { savedProcess ->
                    assert(task == savedProcess.copy(id = task.id))
                }
                .delete(task)
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
        importTaskDAO.verifyAll()
        filesDAO.verifyAll()
    }

    @Test
    fun import_resumeAndUpdate() {
        val tasks: List<ImportTask> = listOf(
            TestImportTask(
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
                    results.callback(tasks[0], listOf(sectionPage))
                    assert(results.tasks == tasks)
                }
                .importFiles(
                    sectionName = sectionName,
                    sectionPage = sectionPage,
                    results = listOf(sectionFile)
                )
                .instance,
            importTaskDAO = importTaskDAO
                .find(
                    sectionName = sectionName,
                    startDate = startDate,
                    endDate = endDate,
                    limit = 100,
                    result = tasks
                )
                .delete(tasks[0])
                .instance,
            filesDAO = filesDAO
                .findFile(sectionFile.id, existingFile)
                .instance
        )

        importService.import(sectionName, startDate, endDate)

        sectionImporter.verifyAll()
        importTaskDAO.verifyAll()
        filesDAO.verifyAll()
    }
}