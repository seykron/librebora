package net.borak.application.model

data class SectionListItemDTO(val fileId: String,
                              val description: String,
                              val category: String,
                              val parentCategory: String,
                              val hasAttachments: Boolean = false)
