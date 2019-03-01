package net.librebora.application

import net.librebora.application.model.FileDTO
import net.librebora.application.model.FilesFactory
import net.librebora.domain.FilesService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** Retrieves a file from a section.
 */
@RestController
@RequestMapping("/bora")
class GetSectionFileController(private val filesService: FilesService,
                               private val filesFactory: FilesFactory) {

    @GetMapping("/files/{fileId}")
    fun retrieveFile(@PathVariable fileId: String): FileDTO {
        return filesFactory.createFile(filesService.findFile(fileId))
    }
}
