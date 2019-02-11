package net.borak.domain.bora.model.companies

import org.joda.time.DateTime

data class Company(val publicationId: String,
                   val name: String,
                   val fileId: String,
                   val creationDate: DateTime,
                   val partners: List<Partner>,
                   val constitution: String?,
                   val description: String?,
                   val capital: String?,
                   val duration: String?,
                   val authorization: String?,
                   val authorities: String?,
                   val address: String?)
