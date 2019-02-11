package net.borak.util.mock

import net.borak.domain.bora.model.sections.SectionListItem
import net.borak.domain.bora.model.sections.Page

class TestSectionPage(private val sessionId: String = "test-session-id",
                      private val items: List<SectionListItem> = listOf()) {

    fun new(): Page {
        return Page(
                sessionId = sessionId,
                items = items
        )
    }
}