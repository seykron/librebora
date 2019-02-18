package net.borak.domain

import net.borak.util.mock.TestCompany
import net.borak.util.mock.TestCompanyDAO
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
