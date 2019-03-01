package net.librebora.util.mock

import com.nhaarman.mockito_kotlin.*
import net.librebora.connector.bora.model.importer.ImportSectionTask
import net.librebora.connector.bora.persistence.ImportSectionTaskDAO
import net.librebora.util.VerifySupport

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