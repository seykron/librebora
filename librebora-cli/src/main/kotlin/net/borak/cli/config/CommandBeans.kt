package net.borak.cli.config

import net.borak.cli.command.ImportCommand
import net.borak.cli.command.LoadDataCommand
import net.borak.cli.command.ParseCommand
import org.springframework.context.support.beans

object CommandBeans {
    fun beans() = beans {
        bean<ImportCommand>()
        bean<ParseCommand>()
        bean<LoadDataCommand>()
    }
}