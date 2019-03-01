package net.librebora.domain

import net.librebora.domain.model.Profile
import net.librebora.domain.persistence.ProfileDAO

class ProfileService(private val profileDAO: ProfileDAO) {

    fun saveOrUpdate(profile: Profile): Profile {
        return profileDAO.saveOrUpdate(profile)
    }

    fun batchInsert(profiles: List<Profile>) {
        profileDAO.batchInsert(profiles)
    }

    fun deleteAll() {
        profileDAO.deleteAll()
    }
}
