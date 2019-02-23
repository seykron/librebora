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
            documentId = "1234",
            email = "foo@bar"
        ).new()

        profileDAO.saveOrUpdate(profile)

        val updatedProfile: Profile = profileDAO.saveOrUpdate(
            TestProfile(
                id = profile.id,
                firstName = "John",
                documentId = "1234",
                email = "bar@foo"
            ).new()
        )

        val currentProfile: Profile? = profileDAO.findByDocumentId(updatedProfile.documentId)[0]
        assert(currentProfile?.email == "bar@foo")
    }
}
