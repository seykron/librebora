package net.borak.support.http

import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.bind.support.WebRequestDataBinder
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import reactor.core.publisher.Mono

object RouterFunctionSupport {

    inline fun<reified RequestType, ResponseType> registerHandler(
        handler: RequestHandler<RequestType, ResponseType>): (ServerRequest) -> Mono<ServerResponse> {

        return { request ->
            val model: RequestType = RequestType::class.java.newInstance()
            val payload: RequestType? = request.bodyToMono(RequestType::class.java).block()
            val dataBinderFactory = WebRequestDataBinder(RequestType::class.java)
            ok().body(fromObject(""))
        }
    }
}