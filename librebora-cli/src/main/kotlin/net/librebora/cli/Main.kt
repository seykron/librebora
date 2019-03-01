package net.librebora.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import net.librebora.cli.command.ImportCommand
import net.librebora.cli.command.LoadDataCommand
import net.librebora.cli.command.ParseCommand
import net.librebora.cli.config.CommandBeans
import net.librebora.config.ConfigBeans
import net.librebora.config.DataSourceBeans
import net.librebora.config.DomainBeans
import net.librebora.config.NaturalLanguageBeans
import net.librebora.support.persistence.DataSourceInitializer
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
        NaturalLanguageBeans.beans().initialize(this)
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
            applicationContext.getBean<ImportCommand>(),
            applicationContext.getBean<ParseCommand>(),
            applicationContext.getBean<LoadDataCommand>()
        )
        .main(args)

    Thread.sleep(2000)
}
