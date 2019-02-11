package net.borak.domain.bora.model.sections

data class Page(val sessionId: String,
                val items: List<SectionListItem>)
