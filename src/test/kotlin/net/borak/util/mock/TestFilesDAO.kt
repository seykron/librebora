package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.domain.model.File
import net.borak.domain.persistence.FilesDAO
import net.borak.util.VerifySupport

class TestFilesDAO: VerifySupport<FilesDAO>() {
    override val instance: FilesDAO = mock()

    fun findFile(fileId: String,
                 result: File?): TestFilesDAO {
        whenever(instance.find(fileId))
            .thenReturn(result)
        verifyCallback {
            verify(instance).find(fileId)
        }
        return this
    }

    fun saveOrUpdate(result: File,
                     callback: ((File) -> Unit)? = null): TestFilesDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedFile = argumentCaptor<File>()
            verify(instance).saveOrUpdate(capturedFile.capture())

            callback?.let {
                callback(capturedFile.firstValue)
            }
        }
        return this
    }
}