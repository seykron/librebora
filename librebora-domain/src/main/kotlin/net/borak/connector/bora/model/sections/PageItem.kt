package net.borak.connector.bora.model.sections

import com.fasterxml.jackson.annotation.JsonProperty

/** Represents an entry in the BORA section list.
 *
 * @property fileId Id of the file.
 * @property description File description.
 * @property category File category.
 * @property parentCategory File parent category.
 * @property hasAttachments Indicates weather this file has attachments or not.
 */
data class PageItem(
    @JsonProperty("id") val fileId: String,
    @JsonProperty("denominacion") val description: String,
    @JsonProperty("rubro") val category: String,
    @JsonProperty("rubroPadre") val parentCategory: String,
    @JsonProperty("tieneAnexos") val hasAttachments: Boolean = false
)
