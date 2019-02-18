package net.borak.connector.bora.model

import org.joda.time.DateTime

data class CompanyInfo(val legalId: String,
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
                       val address: String?)
