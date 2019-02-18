package net.borak.domain.persistence

import net.borak.domain.model.File
import net.borak.domain.model.Section
import net.borak.util.TestDataSource.initTransaction
import org.joda.time.DateTime
import org.junit.Test

class FilesDAOTest {

    private val filesDAO: FilesDAO by lazy {
        initTransaction(FilesDAO())
    }

    @Test
    fun saveOrUpdate() {
        val newFile = File.create(
            section = Section.SECOND,
            fileId = "A784388",
            categoryId = "1110",
            categoryName = "CONSTITUCION SA",
            pdfFile = "2018101202N.pdf",
            text = "test-0",
            publicationDate = DateTime.parse("2018-10-12T00:00:00Z")
        )
        filesDAO.saveOrUpdate(newFile)

        val updatedFile: File = filesDAO.saveOrUpdate(
            File(
                section = Section.SECOND,
                id = newFile.id,
                fileId = "A784388",
                categoryId = "1110",
                categoryName = "CONSTITUCION SA",
                pdfFile = "2018101202N.pdf",
                text = "test-1",
                publicationDate = DateTime.parse("2018-10-12T00:00:00Z")
            )
        )

        val currentFile: File? = filesDAO.find(updatedFile.fileId)
        assert(currentFile?.text == "test-1")
    }
}
