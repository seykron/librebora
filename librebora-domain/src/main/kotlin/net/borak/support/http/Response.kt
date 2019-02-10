package net.borak.support.http

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.cookie.Cookie

data class Response(private val objectMapper: ObjectMapper,
                    val headers: Map<String, String>,
                    val contentType: String,
                    val body: String,
                    val statusCode: Int,
                    val statusLine: String,
                    val cookies: List<Cookie>) {

    fun<T> bodyAs(clazz: Class<T>): T {
        return when (contentType) {
            "application/json" -> objectMapper.readValue(body, clazz)
            else -> throw RuntimeException("Cannot deserialize content type $contentType")
        }
    }
}
