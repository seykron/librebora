package net.borak.application.model

import net.borak.connector.bora.model.*
import net.borak.connector.bora.model.sections.Cursor
import net.borak.connector.bora.model.sections.SectionFile
import net.borak.connector.bora.model.sections.Page

class BoraItemsFactory {

    fun createSectionPage(page: Page,
                          cursor: Cursor): SectionPageDTO {

        val items: List<SectionListItemDTO> = page.items.map { item ->
            SectionListItemDTO(
                fileId = item.fileId,
                description = item.description,
                category = item.category,
                parentCategory = item.parentCategory,
                hasAttachments = item.hasAttachments
            )
        }

        return SectionPageDTO(
            items = items,
            cursor = createCursor(
                sessionId = page.sessionId,
                cursor = cursor
            )
        )
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

    fun createCursor(sessionId: String,
                     cursor: Cursor): CursorDTO {

        return CursorDTO(
            previous = if (cursor.offset > 1) {
                "/bora/sections/${cursor.sectionName}?" +
                        "offset=${cursor.offset - 1}" +
                        "&itemsPerPage=${cursor.itemsPerPage}" +
                        "&date=${cursor.date.toString("yyyyMMdd")}" +
                        "&sessionId=$sessionId"
            } else {
                null
            },
            next = "/bora/sections/${cursor.sectionName}?" +
                    "offset=${cursor.offset + 1}" +
                    "&itemsPerPage=${cursor.itemsPerPage}" +
                    "&date=${cursor.date.toString("yyyyMMdd")}" +
                    "&sessionId=$sessionId"
        )
    }

    fun createError(exception: BoraClientException): BoraErrorDTO {
        return BoraErrorDTO(exception.code, exception.message)
    }
}