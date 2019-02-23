package net.borak.connector.bora.model.sections

/** Represents a section page.
 *
 * @property sessionId Session id to retrieve the next page.
 * @property items List of items in this section page.
 */
data class Page(
    val sessionId: String,
    val items: List<PageItem>
)
