package net.borak.application.model

import net.borak.domain.model.Cursor

class PaginatedResultFactory {

    fun<T> create(
        servicePath: String,
        cursor: Cursor,
        items: List<T>
    ): PaginatedResult<T> {
        val previousPage: String? = if (cursor.pageNumber > 0) {
            buildPath(servicePath, cursor.itemsPerPage, cursor.pageNumber - 1)
        } else {
            null
        }
        val nextPage: String? = if (items.isNotEmpty()) {
            buildPath(servicePath, cursor.itemsPerPage, cursor.pageNumber + 1)
        } else {
            null
        }

        return PaginatedResult(
            items = items,
            previousPage = previousPage,
            nextPage = nextPage
        )
    }

    private fun buildPath(
        servicePath: String,
        itemsPerPage: Int,
        pageNumber: Int
    ): String =
        "${servicePath.removeSuffix("/")}?itemsPerPage=$itemsPerPage&pageNumber=$pageNumber"
}