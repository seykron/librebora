package net.borak.domain.model

import org.joda.time.DateTime
import java.util.*

data class File(val id: UUID,
                val fileId: String,
                val categoryId: String,
                val categoryName: String,
                val publicationDate: DateTime,
                val text: String,
                val pdfFile: String) {

    companion object {
        fun create(fileId: String,
                   categoryId: String,
                   categoryName: String,
                   publicationDate: DateTime,
                   text: String,
                   pdfFile: String): File {

            return File(
                id = UUID.randomUUID(),
                fileId = fileId,
                categoryId = categoryId,
                categoryName = categoryName,
                publicationDate = publicationDate,
                text = text,
                pdfFile = pdfFile
            )
        }
    }
}
