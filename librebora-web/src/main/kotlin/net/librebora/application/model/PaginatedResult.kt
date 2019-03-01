package net.librebora.application.model

/** Represents a generic API list result with pagination.
 * @property previousPage URI to fetch the previous page.
 * @property nextPage URI to fetch the next page.
 */
data class PaginatedResult<T>(val previousPage: String?,
                              val nextPage: String?,
                              val items: List<T>)
