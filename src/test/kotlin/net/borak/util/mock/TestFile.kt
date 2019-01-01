package net.borak.util.mock

import net.borak.domain.model.File
import org.joda.time.DateTime
import java.util.*

class TestFile(private val id: UUID = UUID.randomUUID(),
               private val fileId: String = "A784388",
               private val pdfFile: String = "2018101202N.pdf",
               private val publicationDate: DateTime = DateTime.now(),
               private val categoryId: String = "1110",
               private val categoryName: String = "CONSTITUCION SA",
               private val text: String = "file text in HTML") {

    fun new(): File {
        return File(
            id = id,
            fileId = fileId,
            pdfFile = pdfFile,
            publicationDate = publicationDate,
            categoryId = categoryId,
            categoryName = categoryName,
            text = text
        )
    }
}