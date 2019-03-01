package net.librebora.domain.persistence

import net.librebora.domain.model.Profile
import net.librebora.support.persistence.AbstractEntity
import net.librebora.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object Profiles : UUIDTable(name = "profiles") {
    val documentId = varchar("document_id", 30).primaryKey(2)
    val gender = varchar("gender", 30).primaryKey(3)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val address = varchar("address", 255)
    val birthday = datetime("birthday").nullable()
    val email = varchar("email", 255).nullable()
    val age = integer("age")

    init {
        index(true, documentId, gender)
    }
}

class ProfileEntity(id: EntityID<UUID>) : AbstractEntity<Profile>(id) {
    companion object : AbstractEntityClass<Profile, ProfileEntity>(Profiles)

    var firstName: String by Profiles.firstName
    var lastName: String by Profiles.lastName
    var documentId: String by Profiles.documentId
    var address: String by Profiles.address
    var birthday: DateTime? by Profiles.birthday
    var gender: String by Profiles.gender
    var email: String? by Profiles.email
    var age: Int by Profiles.age

    override fun create(source: Profile): AbstractEntity<Profile> {
        firstName = source.firstName
        lastName = source.lastName
        documentId = source.documentId
        gender = source.gender
        birthday = source.birthday
        return update(source)
    }

    override fun update(source: Profile): ProfileEntity {
        address = source.address
        email = source.email
        age = source.age
        return this
    }

    override fun toDomainType(): Profile {
        return Profile.new(
            firstName = firstName,
            lastName = lastName,
            documentId = documentId,
            address = address,
            birthday = birthday,
            gender = gender,
            email = email,
            age = age
        ).copy(
            id = id.value
        )
    }
}
