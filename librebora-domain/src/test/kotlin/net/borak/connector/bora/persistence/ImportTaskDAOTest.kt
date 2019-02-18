package net.borak.connector.bora.persistence

import net.borak.connector.bora.model.importer.ImportTask
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.mock.TestImportTask
import org.joda.time.DateTime
import org.junit.Test
import java.util.*

class ImportTaskDAOTest {

    private val importTaskDAO: ImportTaskDAO by lazy {
        initTransaction(ImportTaskDAO())
    }

    @Test
    fun saveOrUpdate() {
        val task = TestImportTask().new()
        val savedTask: ImportTask = importTaskDAO.saveOrUpdate(task)
        assert(savedTask == task)
    }

    @Test
    fun findActive() {
        val date: DateTime = DateTime.now().withTimeAtStartOfDay()
        val importTask: ImportTask = TestImportTask(
            sectionName = "primera",
            date = date
        ).new()
        val importTasks: List<ImportTask> = listOf(
            importTask.terminate(),
            importTask.copy(id = UUID.randomUUID()).terminate(),
            importTask.copy(id = UUID.randomUUID()).run(),
            importTask.copy(id = UUID.randomUUID()).run(),
            importTask.copy(id = UUID.randomUUID())
        )
        importTasks.map(importTaskDAO::saveOrUpdate)

        assert(importTasks.subList(0, 5).containsAll(
            importTaskDAO.findActive("primera")
        ))
    }
}
