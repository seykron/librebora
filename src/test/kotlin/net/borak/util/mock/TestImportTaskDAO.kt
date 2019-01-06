package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.service.bora.model.ImportTask
import net.borak.service.bora.persistence.ImportTaskDAO
import net.borak.util.VerifySupport
import org.joda.time.DateTime

class TestImportTaskDAO : VerifySupport<ImportTaskDAO>() {
    override val instance: ImportTaskDAO = mock()

    fun find(sectionName: String,
             startDate: DateTime,
             endDate: DateTime,
             limit: Int,
             offset: Int = 0,
             result: List<ImportTask>): TestImportTaskDAO {
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

    fun save(result: ImportTask,
             callback: ((ImportTask) -> Unit)? = null): TestImportTaskDAO {
        whenever(instance.save(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedProcess = argumentCaptor<ImportTask>()
            verify(instance).save(capturedProcess.capture())

            callback?.let {
                callback(capturedProcess.firstValue)
            }
        }
        return this
    }

    fun delete(importTask: ImportTask): TestImportTaskDAO {
        verifyCallback {
            verify(instance).delete(importTask)
        }
        return this
    }
}