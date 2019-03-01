package net.librebora.util.mock

import com.nhaarman.mockito_kotlin.*
import net.librebora.connector.bora.SectionImporter
import net.librebora.connector.bora.model.sections.SectionFile
import net.librebora.util.VerifySupport

class TestSectionImporter : VerifySupport<SectionImporter>() {

    override val instance: SectionImporter = mock()

    fun importSection(
        results: List<SectionFile>
    ): TestSectionImporter {
        verifyCallback {
            val capturedFileExists = argumentCaptor<(String) -> Boolean>()
            val capturedCallback = argumentCaptor<(SectionFile) -> Unit>()

            verify(instance, times(results.size)).importSection(
                any(), capturedFileExists.capture(), capturedCallback.capture()
            )

            results.forEach { importFileResult ->
                capturedFileExists.firstValue(importFileResult.id)
                capturedCallback.firstValue(importFileResult)
            }
        }

        return this
    }
}
