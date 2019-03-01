package net.librebora.support.http

import org.apache.http.cookie.Cookie

interface HttpResponse {
    val headers: Map<String, String>
    val contentType: String
    val body: String
    val statusCode: Int
    val statusLine: String
    val cookies: List<Cookie>
}
