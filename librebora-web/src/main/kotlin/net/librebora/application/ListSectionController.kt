package net.librebora.application

import net.librebora.application.model.CursorDTO
import net.librebora.application.model.FileDTO
import net.librebora.application.model.FilesFactory
import net.librebora.application.model.PaginatedResult
import net.librebora.domain.FilesService
import net.librebora.domain.model.Section
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
