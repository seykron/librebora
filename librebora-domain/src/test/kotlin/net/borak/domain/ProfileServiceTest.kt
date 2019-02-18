package net.borak.domain

import net.borak.util.mock.TestProfile
import net.borak.util.mock.TestProfileDAO
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
