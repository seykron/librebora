package net.borak.domain.bora.nlp.parser

import net.borak.domain.bora.model.companies.Company
import net.borak.domain.bora.model.companies.Partner
import net.borak.domain.bora.nlp.tokenizer.ClassifiedDocumentTokenizer
import net.borak.domain.bora.nlp.tokenizer.IndexedDocumentTokenizer
import net.borak.domain.bora.nlp.tokenizer.Token
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

class CompanyParser(private val sectionTagger: SectionTagger,
                    private val classifiedDocumentTokenizer: ClassifiedDocumentTokenizer,
                    private val indexedDocumentTokenizer: IndexedDocumentTokenizer,
                    private val partnersParser: PartnersParser,
                    private val fileInfoParser: FileInfoParser) {

    private val logger: Logger = LoggerFactory.getLogger(CompanyParser::class.java)

    fun parse(publicationId: String,
              document: String): Company {
        return Jsoup.parse(document).run {
            logger.info("parsing file $publicationId")

            val companyName: String = select("span:first-child").text()
            val paragraphs = select("p")

            if (paragraphs.isEmpty()) {
                throw ParseException("[$publicationId] No data found for parsing")
            }

            val companyInfo: ByteArray = paragraphs[paragraphs.size - 2].text().toByteArray()
            val fileInfo: FileInfo = fileInfoParser.parse(paragraphs[paragraphs.size - 1].text())

            logger.info("[$publicationId] [$companyName] tokenizing company data")
            val sectionsTokens: List<Token> = tokenizeSections(companyInfo)

            logger.info("[$publicationId] [$companyName] resolving document sections")
            val sections = sectionTagger.resolve(sectionsTokens)

            logger.info("[$publicationId] [$companyName] parsing partners")
            val partners: List<Partner> = partnersParser.parse(document)

            Company(
                publicationId = publicationId,
                name = companyName,
                fileId = fileInfo.fileId,
                creationDate = fileInfo.creationDate,
                partners = partners,
                constitution = getSectionContent(sections, SectionName.CONSTITUTION),
                description = getSectionContent(sections, SectionName.OBJECT),
                capital = getSectionContent(sections, SectionName.CAPITAL),
                duration = getSectionContent(sections, SectionName.DURATION),
                authorization = getSectionContent(sections, SectionName.AUTHORIZATION),
                authorities = getSectionContent(sections, SectionName.AUTHORITIES),
                address = getSectionContent(sections, SectionName.COMPANY_ADDRESS)
            )
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

    private fun getSectionContent(sections: List<SectionInfo>,
                                  sectionName: SectionName): String? {

        return sections.find { sectionInfo ->
            sectionInfo.name == sectionName
        }?.let { sectionInfo ->
            sectionInfo.content.toString(Charset.defaultCharset())
        }
    }
}