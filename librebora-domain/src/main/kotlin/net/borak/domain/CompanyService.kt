package net.borak.domain

import net.borak.domain.model.Company
import net.borak.domain.persistence.CompanyDAO

class CompanyService(private val companyDAO: CompanyDAO) {

    fun saveOrUpdate(company: Company): Company {
        return companyDAO.saveOrUpdate(company)
    }
}