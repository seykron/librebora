package net.librebora.config

import net.librebora.application.model.FilesFactory
import net.librebora.application.model.PaginatedResultFactory
import org.springframework.context.support.beans

object ApplicationBeans {
    fun beans() = beans {
        bean<PaginatedResultFactory>()
        bean<FilesFactory>()
    }
}