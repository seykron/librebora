package net.borak.support.http

import org.apache.http.cookie.Cookie

open class GenericResponse(
    override val headers: Map<String, String>,
    override val contentType: String,
    override val body: String,
    override val statusCode: Int,
    override val statusLine: String,
    override val cookies: List<Cookie>
) : HttpResponse
