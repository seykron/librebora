package net.borak

import net.borak.config.*
import net.borak.domain.files.ImportScheduler
import org.springframework.beans.factory.getBean
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
            DataSourceBeans.beans().initialize(this)
            NaturalLanguageBeans.beans().initialize(this)
            register(WebConfig::class.java)
            refresh()
        }

        val dataSourceInitializer: DataSourceInitializer = context.getBean()
        dataSourceInitializer.init()

        val importScheduler: ImportScheduler = context.getBean()
        importScheduler.start()

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
