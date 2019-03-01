package net.librebora.util.mock

import com.nhaarman.mockito_kotlin.*
import net.librebora.connector.bora.BoraClient
import net.librebora.connector.bora.model.sections.SectionFile
import net.librebora.connector.bora.model.sections.Page
import net.librebora.util.VerifySupport

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

    fun list(vararg results: Page): TestBoraClient {
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