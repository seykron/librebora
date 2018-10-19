package net.borak.domain.bora

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.*
import net.borak.domain.bora.model.*
import net.borak.support.http.RequestBuilder.Companion.POST
import net.borak.support.http.RestClient

class BoraClient(private val restClient: RestClient,
                 private val objectMapper: ObjectMapper) {

    companion object {
        private const val BORA_SECTION_SECOND_LIST_URL: String = "/secciones/secciones.json"
        private const val BORA_SECTION_ITEM_FIRST_URL: String = "/norma/detallePrimera"
        private const val BORA_SECTION_ITEM_SECOND_URL: String = "/norma/detalleSegunda"
        private const val BORA_SECTION_ITEM_THIRD_URL: String = "/norma/detalleTercera"
    }

    fun list(request: SectionListRequest): SectionListPage {
        val jsonPage: String = restClient.execute(
            POST(BORA_SECTION_SECOND_LIST_URL)
                .form(request.formData())
        ).body

        val page: SectionListPage = objectMapper.readValue(jsonPage)
        val jsonTree: JsonNode = objectMapper.readTree(jsonPage)

        return listOf(
            ::resolveError,
            ::resolveItems
        ).fold(page) { previousPage, parse ->
            parse(previousPage, jsonTree)
        }
    }

    fun retrieve(fileId: String): SectionItem {
        return SectionItem("", null, null)
    }

    private fun resolveItems(page: SectionListPage,
                             jsonTree: JsonNode): SectionListPage {

        return if (jsonTree["dataList"].size() > 0) {
            val items: List<SectionListItem> = objectMapper.readValue(jsonTree["dataList"][0].toString())
            page.copy(
                items = items
            )
        } else {
            page
        }
    }

    private fun resolveError(page: SectionListPage,
                             jsonTree: JsonNode): SectionListPage {

        val errorCode: Int = jsonTree["codigoError"].asInt()

        return if (errorCode > 0) {
            page.copy(
                error = BoraError(
                    code = errorCode,
                    message = jsonTree["mensajeError"].asText()
                )
            )
        } else {
            page
        }
    }
}
