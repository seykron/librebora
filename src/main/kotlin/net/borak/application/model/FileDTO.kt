package net.borak.application.model

import net.borak.domain.model.Section
import org.joda.time.DateTime

data class FileDTO(val id: String,
                   val section: Section,
                   val fileId: String,
                   val categoryId: String,
                   val categoryName: String,
                   val publicationDate: DateTime,
                   val text: String,
                   val pdfFile: String)
