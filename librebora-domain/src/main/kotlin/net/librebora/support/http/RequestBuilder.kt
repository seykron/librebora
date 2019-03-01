package net.librebora.support.http

import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.*

/** Builds an HTTP request to execute with an [HttpClient].
 *
 * @property method HTTP method.
 * @property path Request path. The base path and the host is configured by the [HttpClientConfig].
 * @property payload Request payload, if it applies the the HTTP method.
 * @property headers Request headers.
 * @property parameters Query string parameters.
 * @property formFields Form fields if this request submits a web form.
 * @property socketTimeout See [RequestConfig.socketTimeout].
 * @property connectTimeout See [RequestConfig.connectTimeout].
 * @property environment Name of an environment configured in [HttpClientConfig].
 */
data class RequestBuilder(
    val method: String,
    val path: String,
    val payload: Any? = null,
    val headers: Map<String, String> = HashMap(),
    val parameters: Map<String, String> = HashMap(),
    val formFields: Map<String, String> = HashMap(),
    val socketTimeout: Int? = null,
    val connectTimeout: Int? = null,
    val environment: String = HttpClientConfig.DEFAULT_ENVIRONMENT
) {

    companion object {
        /** Creates an HTTP GET request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun GET(path: String): RequestBuilder {
            return RequestBuilder(HttpGet.METHOD_NAME, path)
        }

        /** Builds an HTTP POST request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun POST(path: String): RequestBuilder {
            return RequestBuilder(HttpPost.METHOD_NAME, path)
        }

        /** Builds an HTTP PUT request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun PUT(path: String): RequestBuilder {
            return RequestBuilder(HttpPut.METHOD_NAME, path)
        }

        /** Builds an HTTP PATCH request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun PATCH(path: String): RequestBuilder {
            return RequestBuilder(HttpPatch.METHOD_NAME, path)
        }

        /** Builds an HTTP DELETE request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun DELETE(path: String): RequestBuilder {
            return RequestBuilder(HttpDelete.METHOD_NAME, path)
        }

        /** Builds an HTTP OPTIONS request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun OPTIONS(path: String): RequestBuilder {
            return RequestBuilder(HttpOptions.METHOD_NAME, path)
        }

        /** Builds an HTTP HEAD request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun HEAD(path: String): RequestBuilder {
            return RequestBuilder(HttpHead.METHOD_NAME, path)
        }

        /** Builds an HTTP TRACE request.
         *
         * @param path Request path. The host and base path is configured
         *                           in the underlying [HttpClientConfig].
         */
        fun TRACE(path: String): RequestBuilder {
            return RequestBuilder(HttpTrace.METHOD_NAME, path)
        }
    }

    /** Sets a single header.
     * @param name Header name.
     * @param value Header value.
     */
    fun header(name: String, value: String): RequestBuilder {
        return copy(
            headers = headers + (name to value)
        )
    }

    /** Sets the query parameters.
     * @param queryParams Map from query parameter name to value.
     */
    fun query(queryParams: Map<String, String>): RequestBuilder {
        return copy(
            parameters = parameters + queryParams
        )
    }

    /** Sets the serialized JSON payload.
     * @param jsonPayload Serialized payload.
     */
    fun json(jsonPayload: String): RequestBuilder {
        return copy(
            payload = jsonPayload,
            headers = headers + ("Content-Type" to HttpClient.CONTENT_TYPE_JSON)
        )
    }

    /** Sets an object to serialize as JSON payload.
     * @param payload Request payload.
     */
    fun json(payload: Any): RequestBuilder {
        return copy(
            payload = payload,
            headers = headers + ("Content-Type" to HttpClient.CONTENT_TYPE_JSON)
        )
    }

    /** Sets the form fields to submit a web form.
     * @param fields Form fields names to values.
     */
    fun form(fields: Map<String, String>): RequestBuilder {
        return copy(
            formFields = fields,
            headers = headers + ("Content-Type" to HttpClient.CONTENT_TYPE_FORM)
        )
    }

    /** Sets the form fields to submit a web form.
     * @param fields Form fields names to values.
     */
    fun form(vararg fields: Pair<String, String>): RequestBuilder {
        return form(mapOf(*fields))
    }

    fun socketTimeout(timeout: Int): RequestBuilder {
        return copy(
            socketTimeout = timeout
        )
    }

    fun connectTimeout(timeout: Int): RequestBuilder {
        return copy(
            connectTimeout = timeout
        )
    }

    /** Sets an environment name configured in the underlying [HttpClientConfig].
     * @param environment Name of the environment.
     */
    fun env(environment: String): RequestBuilder {
        return copy(
            environment = environment
        )
    }
}
