package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.service.bora.model.ImportTask
import net.borak.service.bora.persistence.ImportTaskDAO
import net.borak.util.VerifySupport
import org.joda.time.DateTime

class TestImportTaskDAO : VerifySupport<ImportTaskDAO>() {
    override val instance: ImportTaskDAO = mock()

    fun findActive(sectionName: String,
                   result: List<ImportTask>): TestImportTaskDAO {
        whenever(instance.findActive(sectionName)).thenReturn(result)

        verifyCallback {
            verify(instance).findActive(sectionName)
        }
        return this
    }

    fun saveOrUpdate(result: ImportTask,
                     callback: ((List<ImportTask>) -> Unit)? = null): TestImportTaskDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedProcess = argumentCaptor<ImportTask>()
            verify(instance, atLeastOnce()).saveOrUpdate(capturedProcess.capture())

            callback?.let {
                callback(capturedProcess.allValues)
            }
        }
        return this
    }
}