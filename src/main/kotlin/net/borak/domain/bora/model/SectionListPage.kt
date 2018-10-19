package net.borak.domain.bora.model

data class SectionListPage(val id: String,
                           val error: BoraError? = null,
                           val items: List<SectionListItem> = ArrayList())
