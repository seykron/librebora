package net.librebora.config

import com.zaxxer.hikari.HikariDataSource
import net.librebora.connector.bora.persistence.ImportSectionTaskDAO
import net.librebora.domain.persistence.CompanyDAO
import net.librebora.domain.persistence.FilesDAO
import net.librebora.domain.persistence.ProfileDAO
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
        bean<ImportSectionTaskDAO>()
        bean<CompanyDAO>()
        bean<ProfileDAO>()
    }
}
