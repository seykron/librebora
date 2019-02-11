package net.borak.domain.files

import net.borak.domain.bora.model.importer.ImportStatus
import net.borak.domain.bora.model.importer.ImportTask
import net.borak.domain.bora.model.sections.SectionFile
import net.borak.domain.bora.model.sections.Page
import net.borak.domain.files.model.File
import net.borak.util.mock.*
import org.joda.time.DateTime
import org.junit.Test

class ImportServiceTest {

    private val sectionImporter: TestSectionImporter = TestSectionImporter()
    private val importTaskDAO: TestImportTaskDAO = TestImportTaskDAO()
    private val filesDAO: TestFilesDAO = TestFilesDAO()
    private val sectionName: String = "segunda"
    private val date: DateTime = DateTime.now().withTimeAtStartOfDay()
    private val endDate: DateTime = DateTime.now().plusDays(1).withTimeAtStartOfDay()

    @Test
    fun import_createProcessesAndFiles() {
        val task: ImportTask = TestImportTask(
            sectionName = sectionName,
            date = date
        ).new()
        val page: Page = TestSectionPage().new()
        val sectionFile: SectionFile = TestSectionFile().new()
        val newFile: File = TestFile().new()

        val importService = ImportService(
                sectionImporter = sectionImporter
                        .importPages { results ->
                            results.callback(task, listOf(page))
                            assert(listOf(task).containsAll(results.tasks))
                        }
                        .importFiles(
                                sectionName = sectionName,
                                page = page,
                                results = listOf(sectionFile)
                        )
                        .instance,
                importTaskDAO = importTaskDAO
                        .findActive(
                                sectionName = sectionName,
                                result = listOf()
                        )
                        .saveOrUpdate(task) { savedTasks ->
                            assert(savedTasks.size == 3)
                            assert(task == savedTasks[0].copy(id = task.id))
                            assert(savedTasks[2].status == ImportStatus.DONE)
                        }
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

        importService.import(sectionName, date, endDate)

        sectionImporter.verifyAll()
        importTaskDAO.verifyAll()
        filesDAO.verifyAll()
    }

    @Test
    fun import_resumeAndUpdate() {
        val tasks: List<ImportTask> = listOf(
            TestImportTask(
                sectionName = sectionName,
                date = date
            ).new()
        )
        val page: Page = TestSectionPage().new()
        val sectionFile: SectionFile = TestSectionFile().new()
        val existingFile: File = TestFile().new()

        val importService = ImportService(
                sectionImporter = sectionImporter
                        .importPages { results ->
                            results.callback(tasks[0], listOf(page))
                            assert(results.tasks[0] == tasks[0])
                        }
                        .importFiles(
                                sectionName = sectionName,
                                page = page,
                                results = listOf(sectionFile)
                        )
                        .instance,
                importTaskDAO = importTaskDAO
                        .findActive(
                                sectionName = sectionName,
                                result = tasks
                        )
                        .saveOrUpdate(tasks[0]) { savedTasks ->
                            assert(savedTasks.size == 2)
                            assert(savedTasks[1].status == ImportStatus.DONE)
                        }
                        .instance,
                filesDAO = filesDAO
                        .findFile(sectionFile.id, existingFile)
                        .instance
        )

        importService.import(sectionName, date, endDate)

        sectionImporter.verifyAll()
        importTaskDAO.verifyAll()
        filesDAO.verifyAll()
    }
}