package net.borak.domain.bora

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import net.borak.domain.bora.model.*

class ResponseParser(private val objectMapper: ObjectMapper) {

    @Suppress("UNCHECKED_CAST")
    fun<T> parse(jsonResult: String,
                 vararg pipeline: (BoraResult, JsonNode) -> BoraResult): T {

        val result: BoraResult = objectMapper.readValue(jsonResult)
        val jsonTree: JsonNode = objectMapper.readTree(jsonResult)

        return pipeline.fold(result) { previousResult, parse ->
            parse(previousResult, jsonTree)
        }.data as T
    }

    fun parseItemsSecond(result: BoraResult,
                         jsonTree: JsonNode): BoraResult {

        return if (jsonTree["dataList"].size() > 0) {
            val items: List<SectionListItem> = objectMapper.readValue(jsonTree["dataList"][0].toString())

            result.copy(
                data = items
            )
        } else {
            result
        }
    }

    fun parseError(result: BoraResult,
                   jsonTree: JsonNode): BoraResult {

        val errorCode: Int = jsonTree["codigoError"].asInt()

        return if (errorCode > 0) {
            throw BoraClientException(errorCode, jsonTree["mensajeError"].asText())
        } else {
            result
        }
    }

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