package net.borak.config

import com.zaxxer.hikari.HikariDataSource
import net.borak.connector.bora.persistence.ImportTaskDAO
import net.borak.domain.persistence.CompanyDAO
import net.borak.domain.persistence.FilesDAO
import net.borak.domain.persistence.ProfileDAO
import org.jetbrains.exposed.sql.Database
import org.springframework.context.support.beans

object DataSourceBeans {
    fun beans() = beans {
        bean {
            Database.connect(
                datasource = ref()
            )
        }

        bean {
            val dataSourceConfig: DataSourceConfig = ref()

            HikariDataSource().apply {
                jdbcUrl = dataSourceConfig.url
                username = dataSourceConfig.user
                password = dataSourceConfig.password
                driverClassName = dataSourceConfig.driver
            }
        }

        bean<FilesDAO>()
        bean<ImportTaskDAO>()
        bean<CompanyDAO>()
        bean<ProfileDAO>()
    }
}
