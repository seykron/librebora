package net.librebora.connector.bora

import net.librebora.connector.bora.model.sections.ListRequest
import net.librebora.connector.bora.model.sections.Page
import net.librebora.connector.bora.model.sections.SectionFile
import net.librebora.support.http.RequestBuilder.Companion.POST
import net.librebora.support.http.HttpClient

/** Client to retrieve data from the BORA API.
 *
 * @param httpClient HTTP client.
 * @param responseParser Support to parse BORA responses.
 */
class BoraClient(private val httpClient: HttpClient,
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
    }

    /** Retrieves the list of BORA entries for a section.
     * @param request Section listing request.
     */
    fun list(request: ListRequest): Page {
        val jsonPage: String = httpClient.execute(
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
        val jsonFile: String = httpClient.execute(
            POST(url).form(mapOf(
                "id" to fileId
            ))
        ).body

        return responseParser.parse(jsonFile,
            responseParser::parseFile
        )
    }
}
