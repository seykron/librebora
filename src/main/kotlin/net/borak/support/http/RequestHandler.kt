package net.borak.support.http

interface RequestHandler<RequestType, ResponseType> {
    fun handle(request: RequestType): ResponseType
}