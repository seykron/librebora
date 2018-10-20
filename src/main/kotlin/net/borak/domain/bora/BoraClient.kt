package net.borak.domain.bora

import net.borak.domain.bora.model.SectionFile
import net.borak.domain.bora.model.SectionListItem
import net.borak.domain.bora.model.SectionListRequest
import net.borak.support.http.RequestBuilder.Companion.POST
import net.borak.support.http.RestClient

class BoraClient(private val restClient: RestClient,
                 private val responseParser: ResponseParser) {

    companion object {
        const val SECTION_FIRST: String = "primera"
        const val SECTION_SECOND: String = "segunda"
        const val SECTION_THIRD: String = "tercera"

        private const val BORA_SECTION_SECOND_LIST_URL: String = "/secciones/secciones.json"
        private const val BORA_SECTION_FILE_FIRST_URL: String = "/norma/detallePrimera"
        private const val BORA_SECTION_FILE_SECOND_URL: String = "/norma/detalleSegunda"
        private const val BORA_SECTION_FILE_THIRD_URL: String = "/norma/detalleTercera"

        fun validSection(sectionName: String): Boolean {
            return listOf(
                SECTION_FIRST, SECTION_SECOND, SECTION_THIRD
            ).contains(sectionName)
        }
    }

    fun list(request: SectionListRequest): List<SectionListItem> {
        val jsonPage: String = restClient.execute(
            POST(BORA_SECTION_SECOND_LIST_URL)
                .form(request.formData())
        ).body

        return responseParser.parse(jsonPage,
            responseParser::parseError,
            responseParser::parseItemsSecond
        )
    }

    fun retrieve(sectionName: String,
                 fileId: String): SectionFile {

        val url: String = when (sectionName) {
            SECTION_FIRST -> BORA_SECTION_FILE_FIRST_URL
            SECTION_SECOND -> BORA_SECTION_FILE_SECOND_URL
            SECTION_THIRD -> BORA_SECTION_FILE_THIRD_URL
            else -> throw RuntimeException("Unknown section $sectionName")
        }
        val jsonFile: String = restClient.execute(
            POST(url).form(mapOf(
                "id" to fileId
            ))
        ).body

        return responseParser.parse(jsonFile,
            responseParser::parseError,
            responseParser::parseFile
        )
    }
}
