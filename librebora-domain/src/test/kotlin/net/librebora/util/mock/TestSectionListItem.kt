package net.librebora.util.mock

import net.librebora.connector.bora.model.sections.PageItem

class TestSectionListItem(private val fileId: String = "A784388",
                          private val description: String = "some description",
                          private val category: String = "CONSTITUCION SA",
                          private val parentCategory: String = "SOCIEDADES",
                          private val hasAttachments: Boolean = false) {

    fun new(): PageItem {
        return PageItem(
                fileId = fileId,
                description = description,
                category = category,
                parentCategory = parentCategory,
                hasAttachments = hasAttachments
        )
    }
}