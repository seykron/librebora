package net.borak.util.mock

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import net.borak.service.bora.SectionImporter
import net.borak.service.bora.model.ImportProcess
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionPage
import net.borak.util.VerifySupport

class TestSectionImporter : VerifySupport<SectionImporter>() {

    data class ImportPagesResult(val processes: List<ImportProcess>,
                                 val callback: (ImportProcess, List<SectionPage>) -> Unit)

    override val instance: SectionImporter = mock()

    fun importPages(callback: (ImportPagesResult) -> Unit): TestSectionImporter {
        verifyCallback {
            val capturedProcesses = argumentCaptor<List<ImportProcess>>()
            val capturedCallback = argumentCaptor<(ImportProcess, List<SectionPage>) -> Unit>()

            verify(instance).importPages(capturedProcesses.capture(), capturedCallback.capture())

            callback(ImportPagesResult(capturedProcesses.firstValue, capturedCallback.firstValue))
        }

        return this
    }

    fun importFiles(sectionName: String,
                    sectionPage: SectionPage,
                    results: List<SectionFile>): TestSectionImporter {
        whenever(instance.importFiles(sectionName, sectionPage))
            .thenReturn(results)

        verifyCallback {
            verify(instance).importFiles(sectionName, sectionPage)
        }
        return this
    }
}
