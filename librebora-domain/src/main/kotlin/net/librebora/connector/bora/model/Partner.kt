package net.librebora.connector.bora.model

/** Represents a [Company] partner.
 *
 * It only holds the partner document id since it's very hard to extract
 * data from the [net.librebora.connector.bora.nlp.parser.SectionName.PARTNERS]
 * section.
 *
 * Additional data and connections must be resolved later.
 *
 * @see [net.librebora.domain.model.Profile]
 */
data class Partner(val documentId: String)
