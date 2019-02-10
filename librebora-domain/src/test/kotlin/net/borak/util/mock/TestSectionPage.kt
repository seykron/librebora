package net.borak.util.mock

import net.borak.domain.bora.model.SectionListItem
import net.borak.domain.bora.model.SectionPage

class TestSectionPage(private val sessionId: String = "test-session-id",
                      private val items: List<SectionListItem> = listOf()) {

    fun new(): SectionPage {
        return SectionPage(
            sessionId = sessionId,
            items = items
        )
    }
}