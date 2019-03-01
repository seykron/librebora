package net.librebora.util.mock

import net.librebora.domain.model.Profile
import org.joda.time.DateTime
import java.util.*

class TestProfile(private val id: UUID = UUID.randomUUID(),
                  private val firstName: String = "John",
                  private val lastName: String = "Smith",
                  private val documentId: String = "1337",
                  private val address: String = "Wallaby 235 Sidney",
                  private val age: Int = 25,
                  private val birthday: DateTime = DateTime.now().minusYears(25),
                  private val gender: String = "FEMALE",
                  private val email: String? = "foo@bar.net") {

    fun new(): Profile {
        return Profile(
            id = id,
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