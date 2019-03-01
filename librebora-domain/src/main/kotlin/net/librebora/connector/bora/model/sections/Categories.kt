package net.librebora.connector.bora.model.sections

/** Some of the known categories of the BORA files with their underlying name.
 *
 * @property categoryName Name of the category in the BORA.
 */
enum class Categories(val categoryName: String) {
    COMPANIES("CONSTITUCION SA"),
    POLITICAL_PARTIES("PARTIDOS POLITICOS")
}
