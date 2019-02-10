package net.borak.config

import net.borak.domain.bora.persistence.ImportTasks
import net.borak.domain.files.persistence.Files
import net.borak.persistence.TransactionSupport
import org.jetbrains.exposed.sql.SchemaUtils

class DataSourceInitializer(private val config: DataSourceConfig) : TransactionSupport() {

    fun init() {
        if (config.isTest) {
            transaction {
                SchemaUtils.create(Files, ImportTasks)
                SchemaUtils.createMissingTablesAndColumns(Files, ImportTasks)
            }
        }
    }
}
