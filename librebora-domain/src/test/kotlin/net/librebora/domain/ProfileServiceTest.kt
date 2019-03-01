package net.librebora.domain

import net.librebora.util.mock.TestProfile
import net.librebora.util.mock.TestProfileDAO
import org.junit.Test

class ProfileServiceTest {

    private val profileDAO: TestProfileDAO = TestProfileDAO()

    @Test
    fun saveOrUpdate() {
        val profile = TestProfile().new()
        val service = ProfileService(
            profileDAO = profileDAO
                .saveOrUpdate(profile) { results ->
                    assert(results[0] == profile)
                }
                .instance
        )
        service.saveOrUpdate(profile)
        profileDAO.verifyAll()
    }
}
