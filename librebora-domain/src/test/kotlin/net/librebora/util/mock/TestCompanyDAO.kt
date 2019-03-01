package net.librebora.util.mock

import com.nhaarman.mockito_kotlin.*
import net.librebora.domain.model.Company
import net.librebora.domain.persistence.CompanyDAO
import net.librebora.util.VerifySupport

class TestCompanyDAO: VerifySupport<CompanyDAO>() {
    override val instance: CompanyDAO = mock()

    fun saveOrUpdate(result: Company,
                     callback: ((List<Company>) -> Unit)? = null): TestCompanyDAO {
        whenever(instance.saveOrUpdate(any()))
            .thenReturn(result)
        verifyCallback {
            val capturedCompany = argumentCaptor<Company>()
            verify(instance, atLeastOnce()).saveOrUpdate(capturedCompany.capture())

            callback?.let {
                callback(capturedCompany.allValues)
            }
        }
        return this
    }
}
