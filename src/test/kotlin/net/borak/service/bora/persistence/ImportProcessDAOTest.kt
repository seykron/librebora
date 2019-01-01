package net.borak.service.bora.persistence

import net.borak.service.bora.model.ImportProcess
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.TestDataSource.transaction
import net.borak.util.mock.TestImportProcess
import org.junit.Test

class ImportProcessDAOTest {

    private val importProcessDAO: ImportProcessDAO by lazy {
        initTransaction(ImportProcessDAO())
    }

    @Test
    fun save() {
        val importProcess = TestImportProcess().new()
        val savedProcess: ImportProcess = importProcessDAO.save(importProcess)
        assert(savedProcess == importProcess)
    }

    @Test
    fun list() = transaction {
        val importProcesses: List<ImportProcess> = listOf(
            TestImportProcess().new(),
            TestImportProcess().new(),
            TestImportProcess().new(),
            TestImportProcess().new(),
            TestImportProcess().new()
        )
        importProcesses.map(importProcessDAO::save)

        assert(importProcesses.subList(0, 2).containsAll(
            importProcessDAO.list(2, 0)
        ))
        assert(importProcesses.subList(2, 4).containsAll(
            importProcessDAO.list(2, 2)
        ))
        assert(importProcessDAO.list(2, 4).containsAll(listOf(importProcesses[4])))
    }
}
