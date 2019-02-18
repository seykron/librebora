package net.borak.util.mock

import com.nhaarman.mockito_kotlin.*
import net.borak.domain.model.Profile
import net.borak.domain.persistence.ProfileDAO
import net.borak.util.VerifySupport

class TestProfileDAO: VerifySupport<ProfileDAO>() {
    override val instance: ProfileDAO = mock()

    fun saveOrUpdate(result: Profile,
                     callback: ((List<Profile>) -> Unit)? = null): TestProfileDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedEntity = argumentCaptor<Profile>()
            verify(instance, atLeastOnce()).saveOrUpdate(capturedEntity.capture())

            callback?.let {
                callback(capturedEntity.allValues)
            }
        }
        return this
    }
}
