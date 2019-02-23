package net.borak.application.model

import net.borak.domain.model.Cursor
import org.springframework.web.bind.annotation.RequestParam

data class CursorDTO(@RequestParam val itemsPerPage: Int = DEFAULT_LIMIT,
                     @RequestParam val pageNumber: Int = DEFAULT_PAGE) {

    companion object {
        private const val DEFAULT_LIMIT: Int = 100
        private const val DEFAULT_PAGE: Int = 0
    }

    fun toCursor() =
        Cursor.new(
            itemsPerPage = itemsPerPage,
            pageNumber = pageNumber
        )
}
