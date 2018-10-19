package net.borak.application.model

data class SectionListPageDTO(val id: String,
                              val error: BoraErrorDTO? = null,
                              val items: List<SectionListItemDTO> = ArrayList())
