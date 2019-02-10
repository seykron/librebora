package net.borak.config

data class DataSourceConfig(val url: String,
                            val user: String,
                            val password: String,
                            val driver: String,
                            val logStatements: Boolean) {
    val isTest: Boolean = driver == "org.h2.Driver"
}
