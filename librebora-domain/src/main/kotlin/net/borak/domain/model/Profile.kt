package net.borak.domain.model

import org.joda.time.DateTime
import java.util.*

data class Profile(val id: UUID,
                   val firstName: String,
                   val lastName: String,
                   val documentId: String,
                   val address: String,
                   val gender: String,
                   val age: Int,
                   val birthday: DateTime?,
                   val email: String?) {

    companion object {
        fun new(
            firstName: String,
            lastName: String,
            documentId: String,
            address: String,
            gender: String,
            age: Int,
            birthday: DateTime?,
            email: String?
        ): Profile {
            return Profile(
                id = UUID.randomUUID(),
                firstName = firstName,
                lastName = lastName,
                documentId = documentId,
                address = address,
                age = age,
                birthday = birthday,
                gender = gender,
                email = email
            )
        }
    }
}
