package net.librebora.domain.model

/** Represents the current state of a paginated result.
 *
 * @property itemsPerPage Number of elements on a page.
 * @property pageNumber Page number.
 */
data class Cursor(val itemsPerPage: Int,
                  val pageNumber: Int) {

    companion object {
        fun new(itemsPerPage: Int,
                pageNumber: Int): Cursor =
            Cursor(
                itemsPerPage = itemsPerPage,
                pageNumber = pageNumber
            )
    }

    fun nextPage(): Cursor =
        copy(pageNumber = pageNumber + 1)
}
