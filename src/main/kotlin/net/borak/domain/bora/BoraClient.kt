package net.borak.domain.bora

import net.borak.domain.bora.model.SectionFile
import net.borak.domain.bora.model.SectionListItem
import net.borak.domain.bora.model.SectionListRequest
import net.borak.support.http.RequestBuilder.Companion.POST
import net.borak.support.http.RestClient

/** Client to retrieve data from the BORA API.
 *
 * @param restClient HTTP client.
 * @param responseParser Support to parse BORA responses.
 */
class BoraClient(private val restClient: RestClient,
                 private val responseParser: ResponseParser) {

    companion object {
        /** BORA first section name. */
        const val SECTION_FIRST: String = "primera"
        /** BORA second section name. */
        const val SECTION_SECOND: String = "segunda"
        /** BORA third section name. */
        const val SECTION_THIRD: String = "tercera"

        private const val BORA_SECTION_SECOND_LIST_URL: String = "/secciones/secciones.json"
        private const val BORA_SECTION_FILE_FIRST_URL: String = "/norma/detallePrimera"
        private const val BORA_SECTION_FILE_SECOND_URL: String = "/norma/detalleSegunda"
        private const val BORA_SECTION_FILE_THIRD_URL: String = "/norma/detalleTercera"

        /** Determines whether the specified section name is a valid BORA section.
         * @param sectionName Name of the section to verify.
         * @return true if it is a valid section name, false otherwise.
         */
        fun validSection(sectionName: String): Boolean {
            return listOf(
                SECTION_FIRST, SECTION_SECOND, SECTION_THIRD
            ).contains(sectionName)
        }
    }

    /** Retrieves the list of BORA entries for a section.
     * @param request Section listing request.
     */
    fun list(request: SectionListRequest): List<SectionListItem> {
        val jsonPage: String = restClient.execute(
            POST(BORA_SECTION_SECOND_LIST_URL)
                .form(request.formData())
        ).body

        return responseParser.parse(jsonPage,
            responseParser::parseItemsSecond
        )
    }

    /** Retrieves a single file from a section.
     * @param sectionName Name of the section containing the file.
     * @param fileId Id of the required file.
     */
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
            responseParser::parseFile
        )
    }
}
