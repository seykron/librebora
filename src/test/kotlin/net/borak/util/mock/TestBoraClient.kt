package net.borak.util.mock

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.borak.service.bora.BoraClient
import net.borak.service.bora.model.SectionFile

class TestBoraClient {

    val instance: BoraClient = mock()

    fun retrieve(sectionName: String,
                 fileId: String,
                 result: SectionFile): TestBoraClient {
        whenever(instance.retrieve(sectionName, fileId))
            .thenReturn(result)
        return this
    }
}