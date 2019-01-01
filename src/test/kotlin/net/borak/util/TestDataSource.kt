package net.borak.util

import net.borak.config.DataSourceConfig
import net.borak.config.DataSourceInitializer
import net.borak.domain.persistence.TransactionSupport
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.test.util.ReflectionTestUtils
import reactor.core.publisher.toMono
import java.lang.reflect.Field

object TestDataSource {
    val config: DataSourceConfig = DataSourceConfig(
        url = "jdbc:h2:file:./test_db",
        user = "sa",
        password = "",
        driver = "org.h2.Driver",
        logStatements = true
    )
    val db: Database by lazy {
        val connection = Database.connect(
            url = config.url,
            user = config.user,
            password = config.password,
            driver = config.driver
        )
        transaction(connection) {
            exec("DROP ALL OBJECTS")
        }
        val initializer = DataSourceInitializer(config)
        val configField: Field = initializer.javaClass.superclass.getDeclaredField("config")
        configField.isAccessible = true
        configField.set(initializer, config)
        ReflectionTestUtils.setField(initializer, "db", connection)
        initializer.init()

        connection
    }

    fun<T : TransactionSupport> initTransaction(transactionSupport: T): T {
        ReflectionTestUtils.setField(transactionSupport, "db", db)
        ReflectionTestUtils.setField(transactionSupport, "config", config)
        return transactionSupport
    }

    fun transaction(statement: Transaction.() -> Unit) {
        db.toMono()
        org.jetbrains.exposed.sql.transactions.transaction(db) {
            addLogger(StdOutSqlLogger)
            statement()
        }
    }
}
