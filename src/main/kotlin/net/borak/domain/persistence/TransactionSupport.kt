package net.borak.domain.persistence

import net.borak.config.DataSourceConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import javax.inject.Inject

abstract class TransactionSupport {

    @Inject
    private lateinit var db: Database
    @Inject
    private lateinit var config: DataSourceConfig

    fun<T> transaction(statement: Transaction.() -> T): T {
        return org.jetbrains.exposed.sql.transactions.transaction(db) {
            if (config.logStatements) {
                addLogger(StdOutSqlLogger)
            }

            statement()
        }
    }
}
