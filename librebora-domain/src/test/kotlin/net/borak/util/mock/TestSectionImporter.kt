package net.borak.util.mock

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import net.borak.domain.bora.SectionImporter
import net.borak.domain.bora.model.importer.ImportFileResult
import net.borak.domain.bora.model.importer.ImportTask
import net.borak.domain.bora.model.sections.SectionFile
import net.borak.domain.bora.model.sections.Page
import net.borak.util.VerifySupport

class TestSectionImporter : VerifySupport<SectionImporter>() {

    data class ImportPagesResult(val tasks: List<ImportTask>,
                                 val callback: (ImportTask, List<Page>) -> Unit)

    override val instance: SectionImporter = mock()

    fun importPages(callback: (ImportPagesResult) -> Unit): TestSectionImporter {
        verifyCallback {
            val capturedProcesses = argumentCaptor<List<ImportTask>>()
            val capturedCallback = argumentCaptor<(ImportTask, List<Page>) -> Unit>()

            verify(instance).importPages(capturedProcesses.capture(), capturedCallback.capture())

            callback(ImportPagesResult(capturedProcesses.firstValue, capturedCallback.firstValue))
        }

        return this
    }

    fun importFiles(sectionName: String,
                    page: Page,
                    results: List<ImportFileResult>): TestSectionImporter {
        whenever(instance.importFiles(sectionName, page))
            .thenReturn(results)

        verifyCallback {
            verify(instance).importFiles(sectionName, page)
        }
        return this
    }
}
