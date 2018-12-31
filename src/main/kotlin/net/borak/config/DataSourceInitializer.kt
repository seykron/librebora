package net.borak.config

import net.borak.domain.persistence.Files
import net.borak.domain.persistence.TransactionSupport
import org.jetbrains.exposed.sql.SchemaUtils

class DataSourceInitializer(private val config: DataSourceConfig) : TransactionSupport() {

    fun init() {
        if (config.isTest) {
            transaction {
                SchemaUtils.create(Files)
            }
        }
    }
}
