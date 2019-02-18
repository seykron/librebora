package net.borak.util.mock

import net.borak.domain.model.Company
import net.borak.domain.model.Profile
import org.joda.time.DateTime
import java.util.*

class TestCompany(private val id: UUID = UUID.randomUUID(),
                  private val legalId: String = "",
                  private val fileId: String = "",
                  private val name: String = "",
                  private val creationDate: DateTime = DateTime.now().minusDays(10),
                  private val partners: List<Profile> = emptyList(),
                  private val constitution: String? = "",
                  private val description: String? = "",
                  private val capital: String? = "",
                  private val duration: String? = "",
                  private val authorization: String? = "",
                  private val authorities: String? = "",
                  private val address: String? = "") {

    fun new(): Company {
        return Company(
            id = id,
            legalId = legalId,
            fileId = fileId,
            name = name,
            creationDate = creationDate,
            partners = partners,
            constitution = constitution,
            description = description,
            capital = capital,
            duration = duration,
            authorization = authorization,
            authorities = authorities,
            address = address
        )
    }
}