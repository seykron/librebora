package net.borak.domain.persistence

import net.borak.domain.model.Profile
import net.borak.util.TestDataSource.initTransaction
import net.borak.util.mock.TestProfile
import org.junit.Test

class ProfileDAOTest {

    private val profileDAO: ProfileDAO by lazy {
        initTransaction(ProfileDAO())
    }

    @Test
    fun saveOrUpdate() {
        val profile = TestProfile(
            firstName = "Jane",
            documentId = "1234"
        ).new()

        profileDAO.saveOrUpdate(profile)

        val updatedProfile: Profile = profileDAO.saveOrUpdate(
            TestProfile(
                id = profile.id,
                firstName = "John",
                documentId = "1234"
            ).new()
        )

        val currentProfile: Profile? = profileDAO.findByDocumentId(updatedProfile.documentId)[0]
        assert(currentProfile?.firstName == "John")
    }
}
