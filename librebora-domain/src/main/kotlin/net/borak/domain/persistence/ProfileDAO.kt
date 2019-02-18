package net.borak.domain.persistence

import net.borak.domain.model.Profile
import net.borak.support.persistence.EntitySerialization.serialize
import net.borak.support.persistence.TransactionSupport
import org.jetbrains.exposed.exceptions.EntityNotFoundException

class ProfileDAO : TransactionSupport() {

    fun findByDocumentId(documentId: String): List<Profile> = transaction {
        ProfileEntity.find {
            Profiles.documentId eq documentId
        }.map(ProfileEntity::toProfile)
    }

    fun saveOrUpdate(profile: Profile): Profile = transaction {
        try {
            ProfileEntity[profile.id].apply {
                profileData = serialize(profile)
            }
        } catch (cause: EntityNotFoundException) {
            ProfileEntity.new(profile.id) {
                documentId = profile.documentId
                profileData = serialize(profile)
            }
        }.toProfile()
    }
}
