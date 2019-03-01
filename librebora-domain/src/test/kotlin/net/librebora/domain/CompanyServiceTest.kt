package net.librebora.domain

import net.librebora.util.mock.TestCompany
import net.librebora.util.mock.TestCompanyDAO
import org.junit.Test

class CompanyServiceTest {

    private val companyDAO: TestCompanyDAO = TestCompanyDAO()

    @Test
    fun saveOrUpdate() {
        val company = TestCompany().new()
        val service = CompanyService(
            companyDAO = companyDAO
                .saveOrUpdate(company) { results ->
                    assert(results[0] == company)
                }
                .instance
        )
        service.saveOrUpdate(company)
        companyDAO.verifyAll()
    }
}
