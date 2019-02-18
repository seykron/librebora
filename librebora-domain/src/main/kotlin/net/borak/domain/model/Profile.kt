package net.borak.domain.model

import org.joda.time.DateTime
import java.util.*

data class Profile(val id: UUID,
                   val firstName: String,
                   val lastName: String,
                   val documentId: String,
                   val address: String,
                   val birthday: DateTime,
                   val gender: String,
                   val email: String?) {
    companion object {
        fun new(
            firstName: String,
            lastName: String,
            documentId: String,
            address: String,
            birthday: DateTime,
            gender: String,
            email: String?
        ): Profile {
            return Profile(
                id = UUID.randomUUID(),
                firstName = firstName,
                lastName = lastName,
                documentId = documentId,
                address = address,
                birthday = birthday,
                gender = gender,
                email = email
            )
        }
    }
}
