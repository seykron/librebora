package net.borak.config

import net.borak.application.model.FilesFactory
import net.borak.application.model.PaginatedResultFactory
import org.springframework.context.support.beans

object ApplicationBeans {
    fun beans() = beans {
        bean<PaginatedResultFactory>()
        bean<FilesFactory>()
    }
}