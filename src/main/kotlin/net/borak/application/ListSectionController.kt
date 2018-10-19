package net.borak.application

import net.borak.application.model.BoraItemsFactory
import net.borak.application.model.SectionListPageDTO
import net.borak.domain.bora.BoraClient
import net.borak.domain.bora.model.SectionListRequest
import org.joda.time.DateTime
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bora")
class ListSectionController(private val boraClient: BoraClient,
                            private val itemsFactory: BoraItemsFactory) {

    @GetMapping("/sections")
    fun listSection(@RequestParam(required = true) name: String,
                    @RequestParam(required = false, defaultValue = "1") offset: Int,
                    @RequestParam(required = false, defaultValue = "500") itemsPerPage: Int,
                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyyMMdd") date: DateTime?): SectionListPageDTO {

        val resolvedDate: DateTime = date?: DateTime.now()

        val sectionListRequest = when(name) {
            SectionListRequest.SECTION_FIRST -> SectionListRequest.first(
                date = resolvedDate,
                offset = offset,
                itemsPerPage = itemsPerPage
            )
            SectionListRequest.SECTION_SECOND -> SectionListRequest.second(
                date = resolvedDate,
                offset = offset,
                itemsPerPage = itemsPerPage
            )
            SectionListRequest.SECTION_THIRD -> SectionListRequest.third(
                date = resolvedDate,
                offset = offset,
                itemsPerPage = itemsPerPage
            )
            else -> throw RuntimeException("Section not supported $name")
        }

        return itemsFactory.createSectionListPage(name, boraClient.list(sectionListRequest))
    }
}
