package net.borak.util.mock

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.borak.domain.model.File
import net.borak.domain.persistence.FilesDAO

class TestFilesDAO {
    val instance: FilesDAO = mock()

    fun findFile(fileId: String,
                 result: File?): TestFilesDAO {
        whenever(instance.find(fileId))
            .thenReturn(result)
        return this
    }

    fun saveOrUpdate(result: File): TestFilesDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        return this
    }
}