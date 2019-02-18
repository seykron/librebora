package net.borak.domain

import net.borak.domain.model.File
import net.borak.domain.model.FileNotFoundException
import net.borak.domain.model.Section
import net.borak.domain.persistence.FilesDAO

class FilesService(private val filesDAO: FilesDAO) {

    fun findFile(fileId: String): File {
        return filesDAO.find(fileId) ?:
            throw FileNotFoundException("File with id $fileId not found")
    }

    fun list(section: Section): List<File> {
        return filesDAO.list(section)
    }
}
