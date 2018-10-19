package net.borak.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.borak.support.http.ClientConfig
import org.springframework.context.support.beans

object ConfigBeans {
    fun beans() = beans {
        val mainConfig: Config = ConfigFactory.defaultApplication().resolve()

        bean("BoraClientConfig") {
            ClientConfig.create(mainConfig.getConfig("bora-client"))
        }
    }
}