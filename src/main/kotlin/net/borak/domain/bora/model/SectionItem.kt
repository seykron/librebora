package net.borak.domain.bora.model

data class SectionItem(val id: String,
                       val error: BoraError? = null,
                       val detail: SectionDetail?)
