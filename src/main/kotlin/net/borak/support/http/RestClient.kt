package net.borak.support.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import net.borak.support.ObjectMapperFactory
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.*
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

class RestClient private constructor(private val httpClient: CloseableHttpClient,
                                     private val cookieStore: BasicCookieStore,
                                     private val objectMapper: ObjectMapper,
                                     private val protocol: String,
                                     private val host: String,
                                     private val port: Int) {

    companion object {
        const val CONTENT_TYPE_FORM: String = "application/x-www-form-urlencoded"
        const val CONTENT_TYPE_JSON: String = "application/json"

        fun create(config: ClientConfig): RestClient {
            val cookieStore = BasicCookieStore()

            return RestClient(
                httpClient = createHttpClient(config, cookieStore),
                cookieStore = cookieStore,
                objectMapper = resolveObjectMapper(config),
                protocol = config.protocol,
                host = config.host,
                port = config.port
            )
        }

        private fun createHttpClient(config: ClientConfig,
                                     cookieStore: BasicCookieStore): CloseableHttpClient {

            val requestConfig: RequestConfig = RequestConfig.custom()
                    .setMaxRedirects(config.maxRedirects)
                    .setConnectTimeout(config.connectionTimeout)
                    .setSocketTimeout(config.socketTimeout)
                    .setCookieSpec(CookieSpecs.STANDARD)
                    .build()

            return HttpClients.custom()
                    .useSystemProperties()
                    .setDefaultRequestConfig(requestConfig)
                    .setDefaultCookieStore(cookieStore)
                    .build()
        }

        private fun resolveObjectMapper(config: ClientConfig): ObjectMapper {
            return when (config.jsonStrategy) {
                PropertyNamingStrategy.SNAKE_CASE -> ObjectMapperFactory.snakeCaseMapper
                PropertyNamingStrategy.LOWER_CAMEL_CASE -> ObjectMapperFactory.camelCaseMapper
                else -> ObjectMapperFactory.snakeCaseMapper
            }
        }
    }

    fun execute(requestBuilder: RequestBuilder): Response {

        val uriBuilder: URIBuilder = object : URIBuilder() {
            init {
                host = this@RestClient.host
                scheme = this@RestClient.protocol
                port = this@RestClient.port
                path = requestBuilder.path
            }
        }

        requestBuilder.parameters.forEach { (name, value) ->
            uriBuilder.addParameter(name, value)
        }

        val request: HttpUriRequest = when (requestBuilder.method) {
            HttpGet.METHOD_NAME -> HttpGet(uriBuilder.build())
            HttpPost.METHOD_NAME -> HttpPost(uriBuilder.build())
            HttpPut.METHOD_NAME -> HttpPut(uriBuilder.build())
            HttpPatch.METHOD_NAME -> HttpPatch(uriBuilder.build())
            HttpDelete.METHOD_NAME -> HttpDelete(uriBuilder.build())
            else -> throw RuntimeException("Unsupported HTTP method ${requestBuilder.method}")
        }

        if (request is HttpEntityEnclosingRequestBase) {
            request.entity = when (requestBuilder.headers["Content-Type"]) {
                CONTENT_TYPE_FORM -> UrlEncodedFormEntity(requestBuilder.formFields.map {
                    (name, value) ->
                        BasicNameValuePair(name, value)
                })
                else -> StringEntity(requestBuilder.payload?: "")
            }
        }

        return buildResponse(httpClient.execute(request))
    }

    private fun buildResponse(response: CloseableHttpResponse): Response {
        response.use {
            val body: String = response.entity.content.bufferedReader().readText()
            val headers: Map<String, String> = response.allHeaders.map { header ->
                header.name to header.value
            }.toMap()

            return Response(
                objectMapper = objectMapper,
                headers = headers,
                contentType = response.entity.contentType.value,
                body = body,
                statusCode = response.statusLine.statusCode,
                statusLine = response.statusLine.reasonPhrase,
                cookies = cookieStore.cookies
            )
        }
    }
}
