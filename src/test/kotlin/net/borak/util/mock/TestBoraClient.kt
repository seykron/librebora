package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.service.bora.BoraClient
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionPage
import net.borak.util.VerifySupport

class TestBoraClient : VerifySupport<BoraClient>() {

    override val instance: BoraClient = mock()

    fun retrieve(sectionName: String,
                 fileId: String,
                 result: SectionFile): TestBoraClient {
        whenever(instance.retrieve(sectionName, fileId))
            .thenReturn(result)
        verifyCallback {
            verify(instance).retrieve(sectionName, fileId)
        }
        return this
    }

    fun list(vararg results: SectionPage): TestBoraClient {
        whenever(instance.list(any()))
            .thenReturn(results[0], *results.takeLast(results.size - 1).toTypedArray())

        verifyCallback {
            results.forEach { _ ->
                verify(instance, atLeastOnce()).list(any())
            }
        }
        return this
    }
}