package net.librebora.cli.config

import net.librebora.cli.command.ImportCommand
import net.librebora.cli.command.LoadDataCommand
import net.librebora.cli.command.ParseCommand
import org.springframework.context.support.beans

object CommandBeans {
    fun beans() = beans {
        bean<ImportCommand>()
        bean<ParseCommand>()
        bean<LoadDataCommand>()
    }
}