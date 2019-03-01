package net.librebora.domain.persistence

import net.librebora.domain.model.Company
import net.librebora.support.persistence.TransactionSupport

class CompanyDAO : TransactionSupport() {

    fun findByLegalId(legalId: String): Company = transaction {
        CompanyEntity.find {
            Companies.legalId eq legalId
        }.single().toDomainType()
    }

    fun saveOrUpdate(company: Company): Company = transaction {
        CompanyEntity.saveOrUpdate(company.id, company)
    }
}
