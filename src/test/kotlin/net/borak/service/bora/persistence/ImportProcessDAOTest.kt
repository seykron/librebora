package net.borak.service.bora.persistence

import net.borak.service.bora.model.ImportProcess
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.TestDataSource.transaction
import net.borak.util.mock.TestImportProcess
import org.joda.time.DateTime
import org.junit.Test
import java.util.*

class ImportProcessDAOTest {

    private val importProcessDAO: ImportProcessDAO by lazy {
        initTransaction(ImportProcessDAO())
    }

    @Test
    fun save() {
        val importProcess = TestImportProcess().new()
        val savedProcess: ImportProcess = importProcessDAO.save(importProcess)
        assert(savedProcess == importProcess.copy(
            startDate = importProcess.startDate.withTimeAtStartOfDay(),
            endDate = importProcess.endDate.withTimeAtStartOfDay()
        ))
    }

    @Test
    fun list() {
        val startDate: DateTime = DateTime.now().withTimeAtStartOfDay()
        val endDate: DateTime = DateTime.now().plusDays(3).withTimeAtStartOfDay()
        val importProcess: ImportProcess = TestImportProcess(
            sectionName = "primera",
            startDate = startDate,
            endDate = endDate
        ).new()
        val importProcesses: List<ImportProcess> = listOf(
            importProcess,
            importProcess.copy(id = UUID.randomUUID()),
            importProcess.copy(id = UUID.randomUUID()),
            importProcess.copy(id = UUID.randomUUID()),
            importProcess.copy(id = UUID.randomUUID())
        )
        importProcesses.map(importProcessDAO::save)

        assert(importProcesses.subList(0, 2).containsAll(
            importProcessDAO.find("primera", startDate, endDate, 2, 0)
        ))
        assert(importProcesses.subList(2, 4).containsAll(
            importProcessDAO.find("primera", startDate, endDate, 2, 2)
        ))
        assert(
            importProcessDAO.find("primera", startDate, endDate, 2, 4).containsAll(listOf(importProcesses[4]))
        )
    }

    @Test
    fun delete() = transaction {
        val importProcess = TestImportProcess().new()
        importProcessDAO.save(importProcess)
        val savedProcess: ImportProcess = ImportProcessEntity.find {
            ImportProcesses.id eq importProcess.id
        }.map(ImportProcessEntity::toImportProcess)[0]

        assert(savedProcess == importProcess.copy(
            startDate = importProcess.startDate.withTimeAtStartOfDay(),
            endDate = importProcess.endDate.withTimeAtStartOfDay()
        ))

        importProcessDAO.delete(importProcess)

        assert(ImportProcessEntity.find {
            ImportProcesses.id eq importProcess.id
        }.map(ImportProcessEntity::toImportProcess).isEmpty())
    }
}
