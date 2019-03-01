package net.librebora.domain.model

import org.joda.time.DateTime
import java.util.*

data class Company(val id: UUID,
                   val legalId: String,
                   val fileId: String,
                   val name: String,
                   val creationDate: DateTime,
                   val partners: List<Profile>,
                   val constitution: String?,
                   val description: String?,
                   val capital: String?,
                   val duration: String?,
                   val authorization: String?,
                   val authorities: String?,
                   val address: String?) {

    companion object {
        fun new(
            legalId: String,
            fileId: String,
            name: String,
            creationDate: DateTime,
            partners: List<Profile>,
            constitution: String?,
            description: String?,
            capital: String?,
            duration: String?,
            authorization: String?,
            authorities: String?,
            address: String?
        ): Company {
            return Company(
                id = UUID.randomUUID(),
                legalId = legalId,
                name = name,
                fileId = fileId,
                creationDate = creationDate,
                partners = partners,
                constitution = constitution,
                description = description,
                capital = capital,
                duration = duration,
                authorization = authorization,
                authorities = authorities,
                address = address
            )
        }
    }
}
