package net.borak.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.borak.support.persistence.DataSourceInitializer
import net.borak.support.http.ClientConfig
import org.springframework.context.support.beans

object ConfigBeans {
    fun beans() = beans {
        val mainConfig: Config = ConfigFactory.defaultApplication().resolve()

        bean {
            val dbConfig: Config = mainConfig.getConfig("db")

            DataSourceConfig(
                url = dbConfig.getString("url"),
                user = dbConfig.getString("user"),
                password = dbConfig.getString("password"),
                driver = dbConfig.getString("driver"),
                logStatements = dbConfig.getBoolean("log-statements")
            )
        }

        bean("BoraClientConfig") {
            ClientConfig.create(mainConfig.getConfig("bora-client"))
        }

        bean {
            DataSourceInitializer(
                config = ref()
            )
        }
    }
}