package net.borak.support.http

import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.typesafe.config.Config

/** [HttpClient] configuration that uses the typesafe configuration format.
 *
 *  ```
 *  client-config {
 *      protocol: "http"
 *      port: 80
 *      base-path: "/"
 *      connection-timeout: 5000
 *      socket-timeout: 3000
 *      read-timeout: 5000
 *      max-redirects: 5
 *      json-strategy: "SNAKE_CASE"
 *      default-env: "uat"
 *      environments: [{
 *          name: "dev"
 *          host: "localhost"
 *      }, {
 *          name: "uat"
 *          host: "uat-host"
 *          port: 8000
 *      }]
 *  }
 *  ```
 */
data class HttpClientConfig(private val defaultEnvironment: String,
                            private val environments: Map<String, ServiceConfig>) {
    companion object {
        const val DEFAULT_ENVIRONMENT: String = "*"

        private const val PROTOCOL: String = "protocol"
        private const val HOST: String = "host"
        private const val PORT: String = "port"
        private const val BASE_PATH: String = "base-path"
        private const val JSON_STRATEGY: String = "json-strategy"
        private const val MAX_REDIRECTS: String = "max-redirects"
        private const val CONNECTION_TIMEOUT: String = "connection-timeout"
        private const val SOCKET_TIMEOUT: String = "socket-timeout"
        private const val READ_TIMEOUT: String = "read-timeout"
        private const val ENVIRONMENTS: String = "environments"
        private const val DEFAULT_ENVIRONMENT_KEY: String = "default-env"

        fun create(config: Config): HttpClientConfig {
            return if (config.hasPath(ENVIRONMENTS)) {
                HttpClientConfig(
                    defaultEnvironment = config.getString(DEFAULT_ENVIRONMENT_KEY),
                    environments = config.getConfigList(ENVIRONMENTS).fold(emptyMap()) {
                            environments, envConfig ->
                        environments + (
                                envConfig.getString("name") to createServiceConfig(config, envConfig)
                                )
                    }
                )
            } else {
                HttpClientConfig(
                    defaultEnvironment = DEFAULT_ENVIRONMENT,
                    environments = mapOf(
                        DEFAULT_ENVIRONMENT to createServiceConfig(config, config)
                    )
                )
            }
        }

        private fun createServiceConfig(mainConfig: Config,
                                        envConfig: Config): ServiceConfig {
            val jsonStrategyConfig: String = if (envConfig.hasPath(PROTOCOL)) {
                envConfig.getString(JSON_STRATEGY)
            } else {
                mainConfig.getString(JSON_STRATEGY)
            }

            return ServiceConfig(
                protocol = if (envConfig.hasPath(PROTOCOL)) {
                    envConfig.getString(PROTOCOL)
                } else {
                    mainConfig.getString(PROTOCOL)
                },
                host = if (envConfig.hasPath(HOST)) {
                    envConfig.getString(HOST)
                } else {
                    mainConfig.getString(HOST)
                },
                port = if (envConfig.hasPath(PORT)) {
                    envConfig.getInt(PORT)
                } else {
                    mainConfig.getInt(PORT)
                },
                basePath = if (envConfig.hasPath(BASE_PATH)) {
                    envConfig.getString(BASE_PATH)
                } else {
                    mainConfig.getString(BASE_PATH)
                },
                maxRedirects = if (envConfig.hasPath(MAX_REDIRECTS)) {
                    envConfig.getInt(MAX_REDIRECTS)
                } else {
                    mainConfig.getInt(MAX_REDIRECTS)
                },
                connectionTimeout = if (envConfig.hasPath(CONNECTION_TIMEOUT)) {
                    envConfig.getInt(CONNECTION_TIMEOUT)
                } else {
                    mainConfig.getInt(CONNECTION_TIMEOUT)
                },
                socketTimeout = if (envConfig.hasPath(SOCKET_TIMEOUT)) {
                    envConfig.getInt(SOCKET_TIMEOUT)
                } else {
                    mainConfig.getInt(SOCKET_TIMEOUT)
                },
                readTimeout = if (envConfig.hasPath(READ_TIMEOUT)) {
                    envConfig.getInt(READ_TIMEOUT)
                } else {
                    mainConfig.getInt(READ_TIMEOUT)
                },
                jsonStrategy = when (jsonStrategyConfig) {
                    "SNAKE_CASE" -> PropertyNamingStrategy.SNAKE_CASE
                    "CAMEL_CASE" -> PropertyNamingStrategy.LOWER_CAMEL_CASE
                    else -> throw RuntimeException("JSON strategy not supported $jsonStrategyConfig")
                }
            )
        }
    }

    fun serviceConfig(environment: String): ServiceConfig {
        return if (environment == DEFAULT_ENVIRONMENT) {
            environments.getValue(defaultEnvironment)
        } else {
            environments.getValue(environment)
        }
    }
}
