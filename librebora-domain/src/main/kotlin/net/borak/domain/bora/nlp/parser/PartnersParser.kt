package net.borak.domain.bora.nlp.parser

import net.borak.domain.bora.model.companies.Partner

class PartnersParser {

    companion object {
        private val PARTNERS_PARSER: Regex = Regex("DNI[\\s]*(\\d{1,3}\\.?\\d{3}\\.?\\d{3})")
    }

    fun parse(document: String): List<Partner> {
        return PARTNERS_PARSER.findAll(document).let { matchResults ->
            matchResults.map { matchResult ->
                Partner(
                    documentId = matchResult.groupValues[1]
                        .replace(".", "")
                        .trim()
                )
            }.toList()
        }.distinctBy { partner ->
            partner.documentId
        }
    }
}
