package net.borak.util.mock

import net.borak.service.bora.model.SectionListItem

class TestSectionListItem(private val fileId: String = "A784388",
                          private val description: String = "some description",
                          private val category: String = "CONSTITUCION SA",
                          private val parentCategory: String = "SOCIEDADES",
                          private val hasAttachments: Boolean = false) {

    fun new(): SectionListItem {
        return SectionListItem(
            fileId = fileId,
            description = description,
            category = category,
            parentCategory = parentCategory,
            hasAttachments = hasAttachments
        )
    }
}