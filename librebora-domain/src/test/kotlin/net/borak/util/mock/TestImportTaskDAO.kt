package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.connector.bora.model.importer.ImportSectionTask
import net.borak.connector.bora.persistence.ImportSectionTaskDAO
import net.borak.util.VerifySupport

class TestImportTaskDAO : VerifySupport<ImportSectionTaskDAO>() {
    override val instance: ImportSectionTaskDAO = mock()

    fun findActive(sectionName: String,
                   result: List<ImportSectionTask>): TestImportTaskDAO {
        whenever(instance.findActive(sectionName)).thenReturn(result)

        verifyCallback {
            verify(instance).findActive(sectionName)
        }
        return this
    }

    fun saveOrUpdate(result: ImportSectionTask,
                     callback: ((List<ImportSectionTask>) -> Unit)? = null): TestImportTaskDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedProcess = argumentCaptor<ImportSectionTask>()
            verify(instance, atLeastOnce()).saveOrUpdate(capturedProcess.capture())

            callback?.let {
                callback(capturedProcess.allValues)
            }
        }
        return this
    }
}