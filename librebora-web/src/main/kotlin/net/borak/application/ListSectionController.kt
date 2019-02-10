package net.borak.application

import net.borak.application.model.FileDTO
import net.borak.domain.files.model.Section
import net.borak.domain.bora.BoraService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Lists entries from a BORA section.
 */
@RestController
@RequestMapping("/bora")
class ListSectionController(private val boraService: BoraService) {

    @GetMapping("/sections/{sectionName}")
    fun listSection(@PathVariable sectionName: String): List<FileDTO> {

        return boraService.list(
            section = Section.fromName(sectionName)
        ).map { file ->
            FileDTO(
                id = file.id.toString(),
                section = file.section,
                fileId = file.fileId,
                categoryId = file.categoryId,
                categoryName = file.categoryName,
                publicationDate = file.publicationDate,
                text = file.text,
                pdfFile = file.pdfFile
            )
        }
    }
}
