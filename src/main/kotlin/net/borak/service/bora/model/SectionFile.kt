package net.borak.domain.bora.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SectionFile(@JsonProperty("idTramite") val id: String,
                       @JsonProperty("archivoPDF") val pdfFile: String,
                       @JsonProperty("fechaPublicacion") val publicationDate: String,
                       @JsonProperty("idRubro") val categoryId: String,
                       @JsonProperty("rubroDescripcion") val category: String,
                       @JsonProperty("textoCompleto") val text: String)
