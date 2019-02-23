package net.borak.domain

import net.borak.domain.model.Profile
import net.borak.domain.persistence.ProfileDAO

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
