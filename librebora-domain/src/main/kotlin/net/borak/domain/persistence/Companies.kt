package net.borak.domain.persistence

import net.borak.domain.model.Company
import net.borak.support.persistence.EntitySerialization.deserialize
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object Companies : UUIDTable(name = "companies") {
    val legalId = varchar("legal_id", 20).uniqueIndex()
    val fileId = varchar("file_id", Files.FILE_ID_LENGTH).uniqueIndex()
    val creationDate = datetime("creation_date")
    val companyData = text("company_data")
}

class CompanyEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<CompanyEntity>(Companies)
    var legalId: String by Companies.legalId
    var fileId: String by Companies.fileId
    var creationDate: DateTime by Companies.creationDate
    var companyData: String by Companies.companyData

    fun toCompany(): Company {
        return deserialize(companyData)
    }
}
