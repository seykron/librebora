package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.service.bora.model.ImportProcess
import net.borak.service.bora.persistence.ImportProcessDAO
import net.borak.util.VerifySupport
import org.joda.time.DateTime

class TestImportProcessDAO : VerifySupport<ImportProcessDAO>() {
    override val instance: ImportProcessDAO = mock()

    fun find(sectionName: String,
             startDate: DateTime,
             endDate: DateTime,
             limit: Int,
             offset: Int = 0,
             result: List<ImportProcess>): TestImportProcessDAO {
        whenever(instance.find(
            sectionName = sectionName,
            startDate = startDate,
            endDate = endDate,
            limit = limit,
            offset = offset
        )).thenReturn(result)

        verifyCallback {
            verify(instance).find(
                sectionName = sectionName,
                startDate = startDate,
                endDate = endDate,
                limit = limit,
                offset = offset
            )
        }
        return this
    }

    fun save(result: ImportProcess,
             callback: ((ImportProcess) -> Unit)? = null): TestImportProcessDAO {
        whenever(instance.save(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedProcess = argumentCaptor<ImportProcess>()
            verify(instance).save(capturedProcess.capture())

            callback?.let {
                callback(capturedProcess.firstValue)
            }
        }
        return this
    }

    fun delete(importProcess: ImportProcess): TestImportProcessDAO {
        verifyCallback {
            verify(instance).delete(importProcess)
        }
        return this
    }
}