package net.borak.service.bora

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.borak.service.bora.model.*

/** Utilities to parse BORA responses.
 */
class ResponseParser(private val objectMapper: ObjectMapper) {

    /** Parses a result applying a list of parsing functions.
     *
     * It automatically adds the ::assertError function at the beginning of the
     * pipeline to validate error responses.
     *
     * The last parsing function MUST resolve the expected result data type.
     * If it doesn't, it will throw a ClassCastException.
     *
     * @param jsonResult BORA response.
     * @param pipeline List of parsing functions to apply.
     *
     * @return The parsing result data.
     */
    @Suppress("UNCHECKED_CAST")
    fun<T> parse(jsonResult: String,
                 vararg pipeline: (BoraResult, JsonNode) -> BoraResult): T {

        val result: BoraResult = objectMapper.readValue(jsonResult)
        val jsonTree: JsonNode = objectMapper.readTree(jsonResult)

        return pipeline.fold(assertError(result, jsonTree)) { previousResult, parse ->
            parse(previousResult, jsonTree)
        }.data as T
    }

    /** Throws an exception if it detects a response error.
     *
     * @param result Current result context.
     * @param jsonTree Jackson parsing tree.
     */
    fun assertError(result: BoraResult,
                    jsonTree: JsonNode): BoraResult {

        val errorCode: Int = jsonTree["codigoError"].asInt()

        return if (errorCode > 0) {
            throw BoraClientException(errorCode, jsonTree["mensajeError"].asText())
        } else {
            result
        }
    }

    /** Parses a list of entries from the second section.
     * It is a terminal parsing function since it sets the result data.
     * @param result Current result context.
     * @param jsonTree Jackson parsing tree.
     */
    fun parseItemsSecond(result: BoraResult,
                         jsonTree: JsonNode): BoraResult {

        return if (jsonTree["dataList"].size() > 0) {
            val items: List<SectionListItem> = objectMapper.readValue(jsonTree["dataList"][0].toString())

            result.copy(
                data = SectionPage(
                    sessionId = result.sessionId,
                    items = items
                )
            )
        } else {
            result
        }
    }

    /** Parses a section file.
     * It is a terminal parsing function since it sets the result data.
     * @param result Current result context.
     * @param jsonTree Jackson parsing tree.
     */
    fun parseFile(result: BoraResult,
                  jsonTree: JsonNode): BoraResult {
        return if (jsonTree["dataList"].size() > 0) {
            val file: SectionFile = objectMapper.readValue(jsonTree["dataList"].toString(), SectionFile::class.java)

            result.copy(
                data = file
            )
        } else {
            result
        }
    }
}