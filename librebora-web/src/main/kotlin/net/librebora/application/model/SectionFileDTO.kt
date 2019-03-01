package net.librebora.application.model

data class SectionFileDTO(val id: String,
                          val pdfFile: String,
                          val publicationDate: String,
                          val categoryId: String,
                          val category: String,
                          val text: String)
