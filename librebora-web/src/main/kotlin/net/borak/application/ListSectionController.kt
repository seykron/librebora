package net.borak.application

import net.borak.application.model.CursorDTO
import net.borak.application.model.FileDTO
import net.borak.application.model.FilesFactory
import net.borak.application.model.PaginatedResult
import net.borak.domain.FilesService
import net.borak.domain.model.Section
import org.springframework.web.bind.annotation.*

/** Lists entries from a BORA section.
 */
@RestController
@RequestMapping("/bora")
class ListSectionController(private val filesService: FilesService,
                            private val filesFactory: FilesFactory) {

    @GetMapping("/sections/{sectionNumber}")
    fun listSection(
        @PathVariable sectionNumber: Int,
        @ModelAttribute cursor: CursorDTO
    ): PaginatedResult<FileDTO> {

        return filesFactory.createFiles(
            servicePath = "/bora/sections/$sectionNumber",
            cursor = cursor.toCursor(),
            files = filesService.list(
                section = Section.fromNumber(sectionNumber),
                cursor = cursor.toCursor()
            )
        )
    }
}
