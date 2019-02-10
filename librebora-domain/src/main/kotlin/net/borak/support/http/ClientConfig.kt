package net.borak.support.http

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.typesafe.config.Config

data class ClientConfig(val protocol: String,
                        val host: String,
                        val port: Int,
                        val jsonStrategy: PropertyNamingStrategy,
                        val maxRedirects: Int,
                        val connectionTimeout: Int,
                        val socketTimeout: Int,
                        val readTimeout: Int) {
    companion object {
        private const val PROTOCOL: String = "protocol"
        private const val HOST: String = "host"
        private const val PORT: String = "port"
        private const val JSON_STRATEGY: String = "json-strategy"
        private const val MAX_REDIRECTS: String = "max-redirects"
        private const val CONNECTION_TIMEOUT: String = "connection-timeout"
        private const val SOCKET_TIMEOUT: String = "socket-timeout"
        private const val READ_TIMEOUT: String = "read-timeout"

        fun create(config: Config): ClientConfig {
            val jsonStrategyConfig: String = config.getString(JSON_STRATEGY)

            return ClientConfig(
                protocol = config.getString(PROTOCOL),
                host = config.getString(HOST),
                port = config.getInt(PORT),
                maxRedirects = config.getInt(MAX_REDIRECTS),
                connectionTimeout = config.getInt(CONNECTION_TIMEOUT),
                socketTimeout = config.getInt(SOCKET_TIMEOUT),
                readTimeout = config.getInt(READ_TIMEOUT),
                jsonStrategy = when (jsonStrategyConfig) {
                    "SNAKE_CASE" -> PropertyNamingStrategy.SNAKE_CASE
                    "CAMEL_CASE" -> PropertyNamingStrategy.LOWER_CAMEL_CASE
                    else -> throw RuntimeException("JSON strategy not supported $jsonStrategyConfig")
                }
            )
        }
    }
}
