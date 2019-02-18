package net.borak.domain.model

enum class Section(val sectionName: String) {
    FIRST("primera"),
    SECOND("segunda"),
    THIRD("tercera"),
    FOURTH("cuarta");

    companion object {
        fun fromName(sectionName: String): Section {
            return values().find { section ->
                section.sectionName == sectionName
            } ?: throw RuntimeException("Section not found: $sectionName")
        }
    }
}