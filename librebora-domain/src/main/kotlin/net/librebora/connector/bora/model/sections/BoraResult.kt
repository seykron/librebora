package net.librebora.connector.bora.model.sections

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/** Represents the BORA generic response.
 *
 * If [BoraResult#errorCode] and [BoraResult#errorMessage] is present,
 * data will be null. Otherwise, data is never null.
 *
 * The session id is used for pagination.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class BoraResult(
    @JsonProperty("id") val sessionId: String,
    @JsonProperty("codigoError") val errorCode: Int,
    @JsonProperty("mensajeError") val errorMessage: String,
    val data: Any? = null
)
