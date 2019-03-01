package net.librebora.connector.bora.model.sections

import com.fasterxml.jackson.annotation.JsonProperty

/** Represents a BORA section file.
 *
 * Different sections have slightly different formats, it synthesizes common attributes.
 *
 * @property id File id.
 * @property pdfFile Name of the file in PDF format, without the url.
 * @property publicationDate Date this file was published to the BORA.
 * @property categoryId Id of the file category.
 * @property category Description of the file category.
 * @property text Raw file text.
 */
data class SectionFile(
    @JsonProperty("idTramite") val id: String,
    @JsonProperty("archivoPDF") val pdfFile: String,
    @JsonProperty("fechaPublicacion") val publicationDate: String,
    @JsonProperty("idRubro") val categoryId: String,
    @JsonProperty("rubroDescripcion") val category: String,
    @JsonProperty("textoCompleto") val text: String
)
