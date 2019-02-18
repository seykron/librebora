package net.borak.domain.persistence

import net.borak.domain.model.Company
import net.borak.support.persistence.EntitySerialization.serialize
import net.borak.support.persistence.TransactionSupport
import org.jetbrains.exposed.exceptions.EntityNotFoundException

class CompanyDAO : TransactionSupport() {

    fun findByLegalId(legalId: String): Company = transaction {
        CompanyEntity.find {
            Companies.legalId eq legalId
        }.single().toCompany()
    }

    fun saveOrUpdate(company: Company): Company = transaction {
        try {
            CompanyEntity[company.id].apply {
                companyData = serialize(company)
            }
        } catch (cause: EntityNotFoundException) {
            CompanyEntity.new(company.id) {
                legalId = company.legalId
                fileId = company.fileId
                creationDate = company.creationDate
                companyData = serialize(company)
            }
        }.toCompany()
    }
}
