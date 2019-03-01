package net.librebora.domain

import net.librebora.domain.model.Company
import net.librebora.domain.persistence.CompanyDAO

class CompanyService(private val companyDAO: CompanyDAO) {

    fun saveOrUpdate(company: Company): Company {
        return companyDAO.saveOrUpdate(company)
    }
}