package net.librebora.config

import net.librebora.connector.bora.BoraClient
import net.librebora.connector.bora.ResponseParser
import net.librebora.connector.bora.SectionImporter
import net.librebora.domain.*
import net.librebora.support.ObjectMapperFactory
import net.librebora.support.http.HttpClient
import org.springframework.context.support.beans

object DomainBeans {
    fun beans() = beans {
        bean("CamelCaseObjectMapper") {
            ObjectMapperFactory.camelCaseMapper
        }

        bean("SnakeCaseObjectMapper") {
            ObjectMapperFactory.snakeCaseMapper
        }

        bean {
            ResponseParser(ref("CamelCaseObjectMapper"))
        }

        bean {
            BoraClient(
                httpClient = HttpClient.create(ref("BoraClientConfig")),
                responseParser = ref()
            )
        }

        bean<SectionImporter>()
        bean<ImportService>()
        bean<ImportScheduler>()
        bean<FilesService>()
        bean<CompanyService>()
        bean<ProfileService>()
    }
}
