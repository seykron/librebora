package net.librebora.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.librebora.connector.bora.model.CompanyInfo
import net.librebora.connector.bora.model.sections.Categories
import net.librebora.connector.bora.nlp.parser.CompanyParser
import net.librebora.connector.bora.nlp.parser.ParseException
import net.librebora.domain.FilesService
import net.librebora.domain.model.File
import net.librebora.domain.model.Section

class ParseCommand(private val filesService: FilesService,
                   private val companyParser: CompanyParser) : CliktCommand(
    help = "Parses all files from a section and generates structured data models.",
    name = "parse"
) {
    private val section by option("-s", "--section").required()
    private val category by option("-c", "--category").required()

    private val parsers: Map<Categories, (File) -> Unit> = mapOf(
        Categories.COMPANIES to this::parseCompany
    )

    override fun run()  {
        val section: Section = Section.fromName(section)
        val category: Categories = Categories.valueOf(category.toUpperCase())

        if (!parsers.containsKey(category)) {
            echo("Parser not implemented for category $category")
            return
        }

        filesService.listByCategory(
            section = section,
            categoryName = category.categoryName
        ).forEach { file ->
            try {
                parsers.getValue(category)(file)
            } catch (cause: ParseException) {
                echo("Failed to parse file ${file.fileId}: ${cause.message}", err = true)
            }
        }
    }

    private fun parseCompany(file: File) {
        val company: CompanyInfo = companyParser.parse(
            publicationId = file.fileId,
            document = file.text
        )
        echo("${company.fileId},${company.name},${company.address},${company.authorities}")
    }
}
