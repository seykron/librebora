package net.borak.util.mock

import net.borak.service.bora.model.SectionFile

class TestSectionFile(private val id: String = "A784388",
                      private val pdfFile: String = "2018101202N.pdf",
                      private val publicationDate: String = "20181012",
                      private val categoryId: String = "1110",
                      private val category: String = "CONSTITUCION SA",
                      private val text: String = "file text in HTML") {

    fun new(): SectionFile {
        return SectionFile(
            id = id,
            pdfFile = pdfFile,
            publicationDate = publicationDate,
            categoryId = categoryId,
            category = category,
            text = text
        )
    }
}