package net.borak.domain.bora.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class BoraResult(@JsonProperty("id") val sessionId: String,
                      @JsonProperty("codigoError") val errorCode: Int,
                      @JsonProperty("mensajeError") val errorMessage: String,
                      val data: Any? = null)
