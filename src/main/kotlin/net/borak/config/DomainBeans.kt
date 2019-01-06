package net.borak.config

import net.borak.domain.ImportService
import net.borak.service.bora.BoraClient
import net.borak.service.bora.ResponseParser
import net.borak.service.bora.SectionImporter
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

        bean {
            SectionImporter(
                boraClient = ref(),
                importTaskDAO = ref()
            )
        }

        bean {
            ImportService(
                sectionImporter = ref(),
                importTaskDAO = ref(),
                filesDAO = ref()
            )
        }
    }
}
