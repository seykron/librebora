package net.librebora.domain.persistence

import net.librebora.domain.model.Company
import net.librebora.util.TestDataSource.initTransaction
import net.librebora.util.mock.TestCompany
import org.junit.Test

class CompanyDAOTest {

    private val companyDAO: CompanyDAO by lazy {
        initTransaction(CompanyDAO())
    }

    @Test
    fun saveOrUpdate() {
        val company = TestCompany(
            name = "initial name",
            legalId = "1234"
        ).new()

        companyDAO.saveOrUpdate(company)

        val updatedCompany: Company = companyDAO.saveOrUpdate(
            TestCompany(
                id = company.id,
                name = "late name",
                legalId = "1234"
            ).new()
        )

        val currentCompany: Company? = companyDAO.findByLegalId(updatedCompany.legalId)
        assert(currentCompany?.name == "late name")
    }
}
