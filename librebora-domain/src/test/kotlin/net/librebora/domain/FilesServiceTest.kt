package net.librebora.domain

import net.librebora.domain.model.File
import net.librebora.domain.model.FileNotFoundException
import net.librebora.util.mock.TestFile
import net.librebora.util.mock.TestFilesDAO
import org.junit.Test

class FilesServiceTest {

    companion object {
        private const val FILE_ID: String = "A1234B"
    }

    private val filesDAO: TestFilesDAO = TestFilesDAO()

    @Test
    fun findFile_found() {
        val savedFile: File = TestFile().new()
        val boraService = FilesService(
            filesDAO = filesDAO
                .findFile(FILE_ID, savedFile)
                .instance
        )

        val result: File = boraService.findFile(FILE_ID)
        assert(result == savedFile)
    }

    @Test(expected = FileNotFoundException::class)
    fun findFile_not_found() {
        val boraService = FilesService(
            filesDAO = filesDAO
                .findFile(FILE_ID, null)
                .instance
        )

        boraService.findFile(FILE_ID)
    }
}