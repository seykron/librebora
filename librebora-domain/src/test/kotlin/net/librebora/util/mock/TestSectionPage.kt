package net.librebora.util.mock

import net.librebora.connector.bora.model.sections.PageItem
import net.librebora.connector.bora.model.sections.Page

class TestSectionPage(private val sessionId: String = "test-session-id",
                      private val items: List<PageItem> = listOf()) {

    fun new(): Page {
        return Page(
                sessionId = sessionId,
                items = items
        )
    }
}