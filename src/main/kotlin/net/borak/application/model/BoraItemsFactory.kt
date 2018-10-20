package net.borak.application.model

import net.borak.domain.bora.model.BoraClientException
import net.borak.domain.bora.model.SectionFile
import net.borak.domain.bora.model.SectionListItem

class BoraItemsFactory {

    fun createSectionListPage(items: List<SectionListItem>): List<SectionListItemDTO> {

        return items.map { item ->
            SectionListItemDTO(
                fileId = item.fileId,
                description = item.description,
                category = item.category,
                parentCategory = item.parentCategory,
                hasAttachments = item.hasAttachments
            )
        }
    }

    fun createFile(sectionFile: SectionFile): SectionFileDTO {
        return SectionFileDTO(
            id = sectionFile.id,
            pdfFile = sectionFile.pdfFile,
            publicationDate = sectionFile.publicationDate,
            categoryId = sectionFile.categoryId,
            category = sectionFile.category,
            text = sectionFile.text
        )
    }

    fun createError(exception: BoraClientException): BoraErrorDTO {
        return BoraErrorDTO(exception.code, exception.message)
    }
}