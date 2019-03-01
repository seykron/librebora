package net.librebora.connector.bora.model

import org.joda.time.DateTime

/** Represents the company information extracted from files in
 * the BORA section [net.librebora.domain.model.Section.SECOND].
 *
 * It is parsed from the file raw text by [net.librebora.connector.bora.nlp.parser.CompanyParser].
 */
data class CompanyInfo(
    val legalId: String,
    val fileId: String,
    val name: String,
    val creationDate: DateTime,
    val partners: List<Partner>,
    val constitution: String?,
    val description: String?,
    val capital: String?,
    val duration: String?,
    val authorization: String?,
    val authorities: String?,
    val address: String?
)
