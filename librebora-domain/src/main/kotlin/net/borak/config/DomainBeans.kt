package net.borak.config

import net.borak.connector.bora.BoraClient
import net.borak.connector.bora.ResponseParser
import net.borak.connector.bora.SectionImporter
import net.borak.domain.FilesService
import net.borak.domain.ImportScheduler
import net.borak.domain.ImportService
import net.borak.support.ObjectMapperFactory
import net.borak.support.http.RestClient
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
                restClient = RestClient.create(ref("BoraClientConfig")),
                responseParser = ref()
            )
        }

        bean<SectionImporter>()
        bean<ImportService>()
        bean<ImportScheduler>()
        bean<FilesService>()
    }
}
