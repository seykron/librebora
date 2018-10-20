package net.borak.application

import net.borak.application.model.BoraItemsFactory
import net.borak.application.model.SectionFileDTO
import net.borak.domain.bora.BoraClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bora")
class GetSectionItemController(private val boraClient: BoraClient,
                               private val itemsFactory: BoraItemsFactory) {

    @GetMapping("/sections/{sectionName}/{fileId}")
    fun listSection(@PathVariable sectionName: String,
                    @PathVariable fileId: String): SectionFileDTO {

        return itemsFactory.createFile(boraClient.retrieve(sectionName, fileId))
    }
}
