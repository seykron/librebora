package net.borak.domain.persistence

import net.borak.domain.model.Profile
import net.borak.support.persistence.EntitySerialization.deserialize
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import java.util.*

object Profiles : UUIDTable(name = "profiles") {
    val documentId = varchar("document_id", 30)
    val profileData = text("profile_data")
}

class ProfileEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ProfileEntity>(Profiles)
    var documentId: String by Profiles.documentId
    var profileData: String by Profiles.profileData

    fun toProfile(): Profile {
        return deserialize(profileData)
    }
}
