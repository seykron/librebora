package net.borak.domain.bora.nlp.parser

import net.borak.domain.bora.nlp.PredictionService
import net.borak.domain.bora.nlp.tokenizer.ClassifiedDocumentTokenizer
import net.borak.domain.bora.nlp.tokenizer.IndexedDocumentTokenizer
import net.borak.domain.bora.nlp.tokenizer.Token
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CompanyParser(private val predictionService: PredictionService,
                    private val sectionTagger: SectionTagger,
                    private val classifiedDocumentTokenizer: ClassifiedDocumentTokenizer,
                    private val indexedDocumentTokenizer: IndexedDocumentTokenizer) {

    private val logger: Logger = LoggerFactory.getLogger(CompanyParser::class.java)

    fun parse(document: String) {
        Jsoup.parse(document).run {
            val companyName: String = select("span:first-child").text()
            val paragraphs = select("p")
            val companyInfo: ByteArray = paragraphs[paragraphs.size - 2].text().toByteArray()
            val sectionsTokens: List<Token> = tokenizeSections(companyInfo)
            val sections = sectionTagger.resolve(sectionsTokens)

            sections.forEach { sectionInfo ->
                logger.info(sectionInfo.name.toString())
            }
        }
    }

    private fun tokenizeSections(companyInfo: ByteArray): List<Token> {
        var sectionsTokens = indexedDocumentTokenizer.tokenize(companyInfo)

        if (sectionsTokens.isEmpty()) {
            sectionsTokens = classifiedDocumentTokenizer.tokenize(companyInfo)
        }

        if (sectionsTokens.isEmpty()) {
            throw ParseException("Cannot resolve document sections")
        }

        return sectionsTokens
    }
}