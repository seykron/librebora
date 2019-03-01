package net.librebora.domain.persistence

import net.librebora.domain.model.Profile
import net.librebora.support.persistence.TransactionSupport
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteAll

class ProfileDAO : TransactionSupport() {

    fun findByDocumentId(documentId: String): List<Profile> = transaction {
        ProfileEntity.find {
            Profiles.documentId eq documentId
        }.map(ProfileEntity::toDomainType)
    }

    fun saveOrUpdate(profile: Profile): Profile = transaction {
        ProfileEntity.saveOrUpdate(profile.id, profile)
    }

    fun batchInsert(profiles: List<Profile>) = transaction {
        Profiles.batchInsert(profiles) { profile ->
            this[Profiles.firstName] = profile.firstName
            this[Profiles.lastName] = profile.lastName
            this[Profiles.documentId] = profile.documentId
            this[Profiles.address] = profile.address
            this[Profiles.birthday] = profile.birthday
            this[Profiles.gender] = profile.gender
            this[Profiles.email] = profile.email
            this[Profiles.age] = profile.age
        }
    }

    fun deleteAll() = transaction {
        Profiles.deleteAll()
    }
}
