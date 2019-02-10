package net.borak.config

import net.borak.application.model.BoraItemsFactory
import org.springframework.context.support.beans

object ApplicationBeans {
    fun beans() = beans {
        bean<BoraItemsFactory>()
    }
}