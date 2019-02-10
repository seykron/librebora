package net.borak.support.http

import org.apache.http.client.methods.*

data class RequestBuilder(val method: String,
                          val path: String,
                          val payload: String? = null,
                          val headers: Map<String, String> = HashMap(),
                          val parameters: Map<String, String> = HashMap(),
                          val formFields: Map<String, String> = HashMap()) {

    companion object {
        fun GET(path: String): RequestBuilder {
            return RequestBuilder(HttpGet.METHOD_NAME, path)
        }

        fun POST(path: String): RequestBuilder {
            return RequestBuilder(HttpPost.METHOD_NAME, path)
        }

        fun PUT(path: String): RequestBuilder {
            return RequestBuilder(HttpPut.METHOD_NAME, path)
        }

        fun PATCH(path: String): RequestBuilder {
            return RequestBuilder(HttpPatch.METHOD_NAME, path)
        }

        fun DELETE(path: String): RequestBuilder {
            return RequestBuilder(HttpDelete.METHOD_NAME, path)
        }
    }

    fun header(name: String, value: String): RequestBuilder {
        return copy(
            headers = headers + (name to value)
        )
    }

    fun parameter(name: String, value: String): RequestBuilder {
        return copy(
            parameters = parameters + (name to value)
        )
    }

    fun json(payload: String): RequestBuilder {
        return copy(
            payload = payload,
            headers = headers + ("Content-Type" to RestClient.CONTENT_TYPE_JSON)
        )
    }

    fun form(fields: Map<String, String>): RequestBuilder {
        return copy(
            formFields = fields,
            headers = headers + ("Content-Type" to RestClient.CONTENT_TYPE_FORM)
        )
    }
}
