package net.librebora.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import net.librebora.support.persistence.DataSourceInitializer
import net.librebora.support.http.HttpClientConfig
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
            HttpClientConfig.create(mainConfig.getConfig("bora-client"))
        }

        bean {
            DataSourceInitializer(
                config = ref()
            )
        }
    }
}