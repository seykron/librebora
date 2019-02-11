package net.borak.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.borak.cli.command.ImportCommand
import net.borak.cli.config.CommandBeans
import net.borak.config.ConfigBeans
import net.borak.config.DataSourceBeans
import net.borak.config.DataSourceInitializer
import net.borak.config.DomainBeans
import org.springframework.beans.factory.getBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext

class Main : CliktCommand(
    name = "librebora",
    help = "Manages batch operations against the BORA."
) {

    val applicationContext: ApplicationContext = AnnotationConfigApplicationContext {
        ConfigBeans.beans().initialize(this)
        DomainBeans.beans().initialize(this)
        CommandBeans.beans().initialize(this)
        DataSourceBeans.beans().initialize(this)
        refresh()
    }

    init {
        val dataSourceInitializer: DataSourceInitializer = applicationContext.getBean()
        dataSourceInitializer.init()
    }

    override fun run() {
        echo("Welcome to BORA CLI application.")
    }
}

fun main(args: Array<String>) {
    val mainCommand = Main()
    val applicationContext = mainCommand.applicationContext

    mainCommand
        .subcommands(
            applicationContext.getBean<ImportCommand>()
        )
        .main(args)
}
