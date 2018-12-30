package net.borak.domain.bora.model

import com.fasterxml.jackson.databind.JsonNode

data class ParsingContext(val result: BoraResult,
                          val jsonTree: JsonNode)
