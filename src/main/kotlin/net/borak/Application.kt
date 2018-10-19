package net.borak

import net.borak.config.ApplicationBeans
import net.borak.config.ApplicationConfig
import net.borak.config.ConfigBeans
import net.borak.config.DomainBeans
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.reactive.DispatcherHandler
import org.springframework.web.server.WebHandler
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.ipc.netty.http.server.HttpServer

class Application(port: Int = 8080) {

    private val httpHandler: HttpHandler

    private val server: HttpServer

    init {
        val context = AnnotationConfigApplicationContext {
            ConfigBeans.beans().initialize(this)
            DomainBeans.beans().initialize(this)
            ApplicationBeans.beans().initialize(this)
            register(ApplicationConfig::class.java)
            refresh()
        }

        val handler: WebHandler = DispatcherHandler(context)

        server = HttpServer.create(port)
        httpHandler = WebHttpHandlerBuilder
            .webHandler(handler)
            .build()

        server.startAndAwait(ReactorHttpHandlerAdapter(httpHandler))
    }
}

fun main(args: Array<String>) {
    Application()
}
