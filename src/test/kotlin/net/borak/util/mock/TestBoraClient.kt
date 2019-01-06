package net.borak.util.mock

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.borak.service.bora.BoraClient
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionListRequest
import net.borak.service.bora.model.SectionPage

class TestBoraClient {

    val instance: BoraClient = mock()

    fun retrieve(sectionName: String,
                 fileId: String,
                 result: SectionFile): TestBoraClient {
        whenever(instance.retrieve(sectionName, fileId))
            .thenReturn(result)
        return this
    }

    fun list(result:  SectionPage): TestBoraClient {
        whenever(instance.list(any()))
            .thenReturn(result)
        return this
    }
}