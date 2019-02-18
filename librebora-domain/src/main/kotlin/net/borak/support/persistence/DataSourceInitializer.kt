package net.borak.support.persistence

import net.borak.config.DataSourceConfig
import net.borak.connector.bora.persistence.ImportTasks
import net.borak.domain.persistence.Companies
import net.borak.domain.persistence.Files
import net.borak.domain.persistence.Profiles
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table

class DataSourceInitializer(private val config: DataSourceConfig) : TransactionSupport() {

    companion object {
        val Tables: Array<Table> = arrayOf(
            Files,
            ImportTasks,
            Companies,
            Profiles
        )
    }

    fun init() {
        if (config.isTest) {
            transaction {
                SchemaUtils.create(*Tables)
                SchemaUtils.createMissingTablesAndColumns(*Tables)
            }
        }
    }
}
