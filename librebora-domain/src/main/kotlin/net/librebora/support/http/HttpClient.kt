package net.librebora.support.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import net.librebora.support.ObjectMapperFactory
import org.apache.http.HttpEntity
import org.apache.http.client.config.CookieSpecs
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.*
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

/** Configurable HTTP client that uses HTTP Components as backend.
 *
 * Features:
 *
 * - Implements all HTTP 1.1 verbs.
 * - Supports sessions using a [BasicCookieStore].
 * - Supports JSON serialization and deserialization with Jackson.
 *
 * @see [RequestBuilder]
 * @see [JsonResponse]
 */
class HttpClient private constructor(
    private val httpClient: CloseableHttpClient,
    private val cookieStore: BasicCookieStore,
    private val defaultRequestConfig: RequestConfig,
    private val httpClientConfig: HttpClientConfig
) {

    companion object {
        const val CONTENT_TYPE_FORM: String = "application/x-www-form-urlencoded"
        const val CONTENT_TYPE_JSON: String = "application/json"

        fun create(httpClientConfig: HttpClientConfig): HttpClient {
            val cookieStore = BasicCookieStore()
            val requestConfig: RequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                .build()

            return HttpClient(
                httpClient = createHttpClient(requestConfig, cookieStore),
                cookieStore = cookieStore,
                defaultRequestConfig = requestConfig,
                httpClientConfig = httpClientConfig
            )
        }

        private fun createHttpClient(requestConfig: RequestConfig,
                                     cookieStore: BasicCookieStore): CloseableHttpClient {

            return HttpClients.custom()
                .useSystemProperties()
                .setDefaultRequestConfig(requestConfig)
                .setDefaultCookieStore(cookieStore)
                .build()
        }
    }

    fun execute(requestBuilder: RequestBuilder): HttpResponse {
        val serviceConfig: ServiceConfig = httpClientConfig.serviceConfig(requestBuilder.environment)

        val uriBuilder: URIBuilder = URIBuilder().apply {
            host = serviceConfig.host
            scheme = serviceConfig.protocol
            port = serviceConfig.port
            path = serviceConfig.basePath + requestBuilder.path
        }

        requestBuilder.parameters.forEach { (name, value) ->
            uriBuilder.addParameter(name, value)
        }

        val request: HttpRequestBase = when (requestBuilder.method) {
            HttpGet.METHOD_NAME -> HttpGet(uriBuilder.build())
            HttpPost.METHOD_NAME -> HttpPost(uriBuilder.build())
            HttpPut.METHOD_NAME -> HttpPut(uriBuilder.build())
            HttpPatch.METHOD_NAME -> HttpPatch(uriBuilder.build())
            HttpDelete.METHOD_NAME -> HttpDelete(uriBuilder.build())
            HttpHead.METHOD_NAME -> HttpHead(uriBuilder.build())
            HttpOptions.METHOD_NAME -> HttpOptions(uriBuilder.build())
            HttpTrace.METHOD_NAME -> HttpTrace(uriBuilder.build())
            else -> throw RuntimeException("Unsupported HTTP method ${requestBuilder.method}")
        }

        request.config = RequestConfig.copy(defaultRequestConfig)
            .setConnectTimeout(requestBuilder.connectTimeout ?: serviceConfig.connectionTimeout)
            .setSocketTimeout(requestBuilder.socketTimeout ?: serviceConfig.socketTimeout)
            .setConnectionRequestTimeout(serviceConfig.readTimeout)
            .setMaxRedirects(serviceConfig.maxRedirects)
            .build()

        requestBuilder.headers.forEach { (name, value) ->
            request.addHeader(name, value)
        }

        if (request is HttpEntityEnclosingRequestBase) {
            request.entity = resolveEntity(serviceConfig, requestBuilder)
        }

        return buildResponse(serviceConfig, httpClient.execute(request))
    }

    private fun buildResponse(serviceConfig: ServiceConfig,
                              response: CloseableHttpResponse): HttpResponse {
        response.use {
            val body: String = response.entity.content.bufferedReader().readText()
            val headers: Map<String, String> = response.allHeaders.map { header ->
                header.name to header.value
            }.toMap()
            val contentType: String = response.entity?.contentType?.value ?: "text/html"

            return when(contentType) {
                "application/json" -> JsonResponse(
                    objectMapper = resolveObjectMapper(serviceConfig),
                    headers = headers,
                    contentType = contentType,
                    body = body,
                    statusCode = response.statusLine.statusCode,
                    statusLine = response.statusLine.reasonPhrase,
                    cookies = cookieStore.cookies
                )
                else -> GenericResponse(
                    headers = headers,
                    contentType = contentType,
                    body = body,
                    statusCode = response.statusLine.statusCode,
                    statusLine = response.statusLine.reasonPhrase,
                    cookies = cookieStore.cookies
                )
            }
        }
    }

    private fun resolveEntity(serviceConfig: ServiceConfig,
                              requestBuilder: RequestBuilder): HttpEntity {

        return when (requestBuilder.headers["Content-Type"]) {
            CONTENT_TYPE_FORM -> UrlEncodedFormEntity(requestBuilder.formFields.map {
                (name, value) -> BasicNameValuePair(name, value)
            })
            else -> when(requestBuilder.payload) {
                !is String -> {
                    val objectMapper: ObjectMapper = resolveObjectMapper(serviceConfig)
                    val jsonPayload: String = objectMapper.writeValueAsString(requestBuilder.payload)
                    StringEntity(jsonPayload, ContentType.APPLICATION_JSON)
                }
                else -> StringEntity(requestBuilder.payload)
            }
        }
    }

    private fun resolveObjectMapper(serviceConfig: ServiceConfig): ObjectMapper {
        return when (serviceConfig.jsonStrategy) {
            PropertyNamingStrategy.SNAKE_CASE -> ObjectMapperFactory.snakeCaseMapper
            PropertyNamingStrategy.LOWER_CAMEL_CASE -> ObjectMapperFactory.camelCaseMapper
            else -> ObjectMapperFactory.snakeCaseMapper
        }
    }
}
