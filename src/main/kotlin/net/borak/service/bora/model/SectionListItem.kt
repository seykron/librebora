package net.borak.domain.bora.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SectionListItem(@JsonProperty("id") val fileId: String,
                           @JsonProperty("denominacion") val description: String,
                           @JsonProperty("rubro") val category: String,
                           @JsonProperty("rubroPadre") val parentCategory: String,
                           @JsonProperty("tieneAnexos") val hasAttachments: Boolean = false)
