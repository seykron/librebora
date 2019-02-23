package net.borak.support.http

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.cookie.Cookie

/** An [HttpResponse] that supports JSON deserialization for the payload.
 *
 * @property headers Response headers.
 * @property contentType Response payload content type.
 * @property body Response payload.
 * @property statusCode HTTP status code.
 * @property statusLine HTTP status line.
 * @property cookies Cookies set in the response.
 */
data class JsonResponse(
    private val objectMapper: ObjectMapper,
    override val headers: Map<String, String>,
    override val contentType: String,
    override val body: String,
    override val statusCode: Int,
    override val statusLine: String,
    override val cookies: List<Cookie>
) : HttpResponse {

    /** Deserializes the payload as JSON.
     *
     * It uses the JSON strategy configured for the service or request.
     *
     * @param clazz Type to create from the JSON payload.
     * @return the deserialized JSON as an instance of [clazz].
     */
    fun<T> bodyAs(clazz: Class<T>): T {
        return when (contentType) {
            "application/json" -> objectMapper.readValue(body, clazz)
            else -> throw RuntimeException("Cannot deserialize content type $contentType")
        }
    }
}