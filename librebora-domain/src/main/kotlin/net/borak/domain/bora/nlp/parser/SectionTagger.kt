package net.borak.domain.bora.nlp.parser

import net.borak.domain.bora.nlp.DocumentStream
import net.borak.domain.bora.nlp.PredictionService
import net.borak.domain.bora.nlp.tokenizer.Token
import net.borak.domain.bora.nlp.parser.SectionName.*

import java.nio.charset.Charset

class SectionTagger(private val predictionService: PredictionService) {

    companion object {
        private const val PARTNERS_KEY: String = "DNI"
        private val CONSTITUTION_KEYS: List<String> = listOf("CONSTITUCION")
        private val CAPITAL_REGEX: Regex = Regex("^\\s*\\\$\\s?[\\d\\\\.]+[,.\\s]*")
        private val DURATION_REGEX: Regex = Regex("^\\s*\\d{1,2}(\\s)*año(s)?.*")
        private val AUTHORITIES_REGEX: Regex = Regex("^.*(presidente).*")
        private val BUSINESS_YEAR_REGEXES: List<Regex> = listOf(
            Regex("^\\s*\\d{1,2}/\\d{1,2}.*"),
            Regex("^\\s*\\d{1,2}.+(enero|febrero|marzo|abril|mayo|junio|julio|agosto|septiembre|setiembre|octubre|noviembre|diciembre)")
        )
    }

    private val sectionMatchers: Map<SectionName, (Int, Token) -> Boolean> = mapOf(
        CONSTITUTION to this::isConstitution,
        PARTNERS to this::isPartners,
        CAPITAL to this::isCapital,
        DURATION to this::isDuration,
        BUSINESS_YEAR_END to this::isBusinessYear,
        AUTHORITIES to this::isAuthorites,
        AUTHORIZATION to this::defaultTagger,
        FILE to this::defaultTagger,
        COMPANY_NAME to this::defaultTagger,
        COMPANY_ADDRESS to this::defaultTagger,
        ADMINISTRATION to this::defaultTagger,
        AUDIT to this::defaultTagger
    )

    fun resolve(sections: List<Token>): List<SectionInfo> {

        return sections.mapIndexed { index, sectionToken ->
            val resolvedSections: List<SectionName> = sectionMatchers.filter { entry ->
                val sectionName = entry.key
                val matcher = entry.value

                hasSectionName(sectionName, sectionToken) || matcher(index, sectionToken)
            }.keys.toList()

            when {
                resolvedSections.size > 1 -> throw ParseException(
                    "More than one section matches for the same document: ${resolvedSections.joinToString(", ")}"
                )
                resolvedSections.isEmpty() -> SectionInfo(
                    name = SectionName.UNKNOWN,
                    content = sectionToken.data
                )
                else -> SectionInfo(
                    name = resolvedSections.first(),
                    content = sectionToken.data
                )
            }
        }
    }

    private fun isConstitution(index: Int,
                               sectionToken: Token): Boolean {

        val matchesConstitutionDate: Boolean = index < 3 && BUSINESS_YEAR_REGEXES.any { regex ->
            DocumentStream(sectionToken.data).contains(regex)
        }

        return matchesConstitutionDate || predictionService.predict(
            text = sectionToken.data.toString(Charset.defaultCharset()),
            candidateClassifiers = CONSTITUTION_KEYS
        ) != null
    }

    private fun isPartners(index: Int,
                           sectionToken: Token): Boolean {
        return DocumentStream(sectionToken.data).contains(PARTNERS_KEY.toByteArray())
    }

    private fun isCapital(index: Int,
                          sectionToken: Token): Boolean {
        return DocumentStream(sectionToken.data).contains(CAPITAL_REGEX)
    }

    private fun isBusinessYear(index: Int,
                               sectionToken: Token): Boolean {
        return index > 3 && BUSINESS_YEAR_REGEXES.any { regex ->
            DocumentStream(sectionToken.data).contains(regex)
        }
    }

    private fun isDuration(index: Int,
                           sectionToken: Token): Boolean {
        return DocumentStream(sectionToken.data).contains(DURATION_REGEX)
    }

    private fun isAuthorites(index: Int,
                             sectionToken: Token): Boolean {
        return DocumentStream(sectionToken.data).contains(AUTHORITIES_REGEX)
    }

    private fun defaultTagger(index: Int,
                              sectionToken: Token): Boolean {
        return false
    }

    private fun hasSectionName(sectionName: SectionName,
                               sectionToken: Token): Boolean {
        return sectionToken.name.toString(Charset.defaultCharset()) == sectionName.name
    }
}
