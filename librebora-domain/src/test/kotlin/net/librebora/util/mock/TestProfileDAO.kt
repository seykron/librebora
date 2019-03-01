package net.librebora.util.mock

import com.nhaarman.mockito_kotlin.*
import net.librebora.domain.model.Profile
import net.librebora.domain.persistence.ProfileDAO
import net.librebora.util.VerifySupport

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
