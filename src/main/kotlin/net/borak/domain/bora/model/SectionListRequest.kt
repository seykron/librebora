package net.borak.domain.bora.model

import net.borak.domain.bora.BoraClient
import org.joda.time.DateTime

data class SectionListRequest(val sectionName: String,
                              val subCategory: String,
                              val date: DateTime,
                              val offset: Int,
                              val itemsPerPage: Int,
                              val sessionId: String) {

    companion object {

        const val SECTION_FIRST: String = "primera"
        const val SECTION_SECOND: String = "segunda"
        const val SECTION_THIRD: String = "tercera"
        private const val CATEGORY_DEFAULT: String = "all"

        private const val FIELD_SECTION_NAME: String = "nombreSeccion"
        private const val FIELD_SUB_CATEGORY: String = "subCat"
        private const val FIELD_OFFSET: String = "offset"
        private const val FIELD_ITEMS_PER_PAGE: String = "itemsPerPage"
        private const val FIELD_DATE: String = "fecha"
        private const val FIELD_SESSION_ID: String = "idSesion"

        fun first(date: DateTime,
                  offset: Int,
                  itemsPerPage: Int): SectionListRequest {

            return createRequest(
                sectionName = SECTION_FIRST,
                offset = offset,
                itemsPerPage = itemsPerPage,
                date = date
            )
        }

        fun second(date: DateTime,
                   offset: Int,
                   itemsPerPage: Int): SectionListRequest {

            return createRequest(
                sectionName = SECTION_SECOND,
                offset = offset,
                itemsPerPage = itemsPerPage,
                date = date
            )
        }

        fun third(date: DateTime,
                  offset: Int,
                  itemsPerPage: Int): SectionListRequest {

            return createRequest(
                sectionName = SECTION_THIRD,
                offset = offset,
                itemsPerPage = itemsPerPage,
                date = date
            )
        }

        private fun createRequest(sectionName: String,
                                  date: DateTime,
                                  offset: Int,
                                  itemsPerPage: Int): SectionListRequest {
            return SectionListRequest(
                sectionName = sectionName,
                subCategory = CATEGORY_DEFAULT,
                offset = offset,
                itemsPerPage = itemsPerPage,
                date = date,
                sessionId = ""
            )
        }
    }

    fun next(nextSessionId: String): SectionListRequest {
        return copy(
            offset = offset + 1,
            sessionId = nextSessionId
        )
    }

    fun formData(): Map<String, String> {
        return mapOf(
            FIELD_SECTION_NAME to sectionName,
            FIELD_SUB_CATEGORY to subCategory,
            FIELD_OFFSET to offset.toString(),
            FIELD_ITEMS_PER_PAGE to itemsPerPage.toString(),
            FIELD_DATE to date.toString("yyyyMMdd"),
            FIELD_SESSION_ID to sessionId
        )
    }
}
