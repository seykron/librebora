package net.borak.service.bora.persistence

import net.borak.service.bora.model.ImportTask
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.TestDataSource.transaction
import net.borak.util.mock.TestImportTask
import org.joda.time.DateTime
import org.junit.Test
import java.util.*

class ImportTaskDAOTest {

    private val importTaskDAO: ImportTaskDAO by lazy {
        initTransaction(ImportTaskDAO())
    }

    @Test
    fun save() {
        val task = TestImportTask().new()
        val savedTask: ImportTask = importTaskDAO.save(task)
        assert(savedTask == task.copy(
            startDate = task.startDate.withTimeAtStartOfDay(),
            endDate = task.endDate.withTimeAtStartOfDay()
        ))
    }

    @Test
    fun list() {
        val startDate: DateTime = DateTime.now().withTimeAtStartOfDay()
        val endDate: DateTime = DateTime.now().plusDays(3).withTimeAtStartOfDay()
        val importTask: ImportTask = TestImportTask(
            sectionName = "primera",
            startDate = startDate,
            endDate = endDate
        ).new()
        val importTasks: List<ImportTask> = listOf(
            importTask,
            importTask.copy(id = UUID.randomUUID()),
            importTask.copy(id = UUID.randomUUID()),
            importTask.copy(id = UUID.randomUUID()),
            importTask.copy(id = UUID.randomUUID())
        )
        importTasks.map(importTaskDAO::save)

        assert(importTasks.subList(0, 2).containsAll(
            importTaskDAO.find("primera", startDate, endDate, 2, 0)
        ))
        assert(importTasks.subList(2, 4).containsAll(
            importTaskDAO.find("primera", startDate, endDate, 2, 2)
        ))
        assert(
            importTaskDAO.find("primera", startDate, endDate, 2, 4).containsAll(listOf(importTasks[4]))
        )
    }

    @Test
    fun delete() = transaction {
        val task = TestImportTask().new()
        importTaskDAO.save(task)
        val savedTask: ImportTask = ImportTaskEntity.find {
            ImportTasks.id eq task.id
        }.map(ImportTaskEntity::toImportTask)[0]

        assert(savedTask == task.copy(
            startDate = task.startDate.withTimeAtStartOfDay(),
            endDate = task.endDate.withTimeAtStartOfDay()
        ))

        importTaskDAO.delete(task)

        assert(ImportTaskEntity.find {
            ImportTasks.id eq task.id
        }.map(ImportTaskEntity::toImportTask).isEmpty())
    }
}
