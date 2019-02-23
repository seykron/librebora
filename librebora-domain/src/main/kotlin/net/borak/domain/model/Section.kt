package net.borak.domain.model

enum class Section(val sectionName: String,
                   val number: Int) {
    FIRST("primera", 1),
    SECOND("segunda", 2),
    THIRD("tercera", 3),
    FOURTH("cuarta", 4);

    companion object {
        fun fromName(sectionName: String): Section {
            return values().find { section ->
                section.sectionName == sectionName
            } ?: throw RuntimeException("Section not found: $sectionName")
        }

        fun fromNumber(number: Int): Section {
            return values().find { section ->
                section.number == number
            } ?: throw RuntimeException("Section not found: $number")
        }
    }
}