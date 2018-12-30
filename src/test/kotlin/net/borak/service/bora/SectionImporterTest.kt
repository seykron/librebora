package net.borak.domain.bora

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import net.borak.domain.bora.model.SectionPage
import org.joda.time.DateTime
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SectionImporterTest {

    private val client: BoraClient = mock()

    @Test
    fun import() {
        val page = SectionPage("", listOf())
        whenever(client.list(any())).thenReturn(page)

        val importer = SectionImporter(client)
        val results: List<SectionPage> = importer.import("segunda", DateTime.now(), DateTime.now().plusDays(2))

        assert(results.size == 3)
        assert(results[0] == page)
        assert(results[1] == page)
        assert(results[2] == page)
    }
}
