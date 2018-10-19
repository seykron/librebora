package net.borak.application.model

import net.borak.domain.bora.model.BoraError
import net.borak.domain.bora.model.SectionListItem
import net.borak.domain.bora.model.SectionListPage

class BoraItemsFactory {

    fun createSectionListPage(sectionName: String,
                              page: SectionListPage): SectionListPageDTO {

        return SectionListPageDTO(
            id = page.id,
            items = page.items.map { item ->
                createSectionListItem(sectionName, item)
            },
            error = createError(page.error)
        )
    }

    fun createSectionListItem(sectionName: String,
                              item: SectionListItem): SectionListItemDTO {

        return SectionListItemDTO(
            id = "$sectionName-${item.fileId}",
            fileId = item.fileId,
            description = item.description,
            category = item.category,
            parentCategory = item.parentCategory,
            hasAttachments = item.hasAttachments
        )
    }

    fun createError(error: BoraError?): BoraErrorDTO? {
        return if (error == null) {
            null
        } else {
            BoraErrorDTO(error.code, error.message)
        }
    }
}