package net.borak.domain.bora.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SectionDetail(@JsonProperty("archivoPDF") val file: String,
                         @JsonProperty("fechaPublicacion") val publicationDate: String?,
                         @JsonProperty("idRubro") val categoryId: String,
                         @JsonProperty("idTramite") val fileId: String,
                         @JsonProperty("rubroDescripcion") val category: String,
                         @JsonProperty("textoCompleto") val text: String)
