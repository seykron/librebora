package net.borak.connector.bora.persistence

import net.borak.connector.bora.model.importer.ImportSectionTask
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.mock.TestImportTask
import org.joda.time.DateTime
import org.junit.Test
import java.util.*

class ImportSectionTaskDAOTest {

    private val importSectionTaskDAO: ImportSectionTaskDAO by lazy {
        initTransaction(ImportSectionTaskDAO())
    }

    @Test
    fun saveOrUpdate() {
        val task = TestImportTask().new()
        val savedTask: ImportSectionTask = importSectionTaskDAO.saveOrUpdate(task)
        assert(savedTask == task)
    }

    @Test
    fun findActive() {
        val date: DateTime = DateTime.now().withTimeAtStartOfDay()
        val importTask: ImportSectionTask = TestImportTask(
            sectionName = "primera",
            date = date
        ).new()
        val importTasks: List<ImportSectionTask> = listOf(
            importTask.terminate(),
            importTask.copy(id = UUID.randomUUID()).terminate(),
            importTask.copy(id = UUID.randomUUID()).run(),
            importTask.copy(id = UUID.randomUUID()).run(),
            importTask.copy(id = UUID.randomUUID())
        )
        importTasks.map(importSectionTaskDAO::saveOrUpdate)

        assert(importTasks.subList(0, 5).containsAll(
            importSectionTaskDAO.findActive("primera")
        ))
    }
}
