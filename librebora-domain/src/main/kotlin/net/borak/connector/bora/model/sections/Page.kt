package net.borak.connector.bora.model.sections

data class Page(val sessionId: String,
                val items: List<SectionListItem>)
