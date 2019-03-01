package net.librebora.support.persistence

import net.librebora.config.DataSourceConfig
import net.librebora.connector.bora.persistence.ImportTasks
import net.librebora.domain.persistence.Companies
import net.librebora.domain.persistence.Files
import net.librebora.domain.persistence.Profiles
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
