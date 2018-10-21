package net.borak.application

import net.borak.application.model.BoraItemsFactory
import net.borak.application.model.SectionPageDTO
import net.borak.domain.bora.BoraClient
import net.borak.domain.bora.model.Cursor
import net.borak.domain.bora.model.SectionListRequest
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*

/** Lists entries from a BORA section.
 */
@RestController
@RequestMapping("/bora")
class ListSectionController(private val boraClient: BoraClient,
                            private val itemsFactory: BoraItemsFactory) {

    @GetMapping("/sections/{sectionName}")
    fun listSection(@PathVariable sectionName: String,
                    @RequestParam(required = false, defaultValue = "1") offset: Int,
                    @RequestParam(required = false, defaultValue = "500") itemsPerPage: Int,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") date: DateTime?,
                    @RequestParam(required = false) sessionId: String?): SectionPageDTO {

        require(BoraClient.validSection(sectionName))

        val resolvedDate: DateTime = date?: DateTime.now()
        val sectionListRequest = SectionListRequest.create(
            sectionName = sectionName,
            date = resolvedDate,
            offset = offset,
            itemsPerPage = itemsPerPage,
            sessionId = sessionId ?: ""
        )

        return itemsFactory.createSectionPage(
            page = boraClient.list(sectionListRequest),
            cursor = Cursor(sectionName, resolvedDate, offset, itemsPerPage)
        )
    }
}
