package net.librebora.domain

import net.librebora.domain.model.Cursor
import net.librebora.domain.model.File
import net.librebora.domain.model.FileNotFoundException
import net.librebora.domain.model.Section
import net.librebora.domain.persistence.FilesDAO

class FilesService(private val filesDAO: FilesDAO) {

    fun findFile(fileId: String): File {
        return filesDAO.find(fileId) ?:
            throw FileNotFoundException("File with id $fileId not found")
    }

    fun list(section: Section,
             cursor: Cursor): List<File> =
        filesDAO.list(
            section = section,
            cursor = cursor
        )

    fun listByCategory(
        section: Section,
        categoryName: String
    ): List<File> {
        return filesDAO.listByCategory(section, categoryName)
    }
}
