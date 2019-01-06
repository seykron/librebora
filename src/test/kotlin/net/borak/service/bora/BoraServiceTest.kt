package net.borak.service.bora

import net.borak.domain.model.File
import net.borak.domain.model.FileNotFoundException
import net.borak.util.mock.TestFile
import net.borak.util.mock.TestFilesDAO
import org.junit.Test

class BoraServiceTest {

    companion object {
        private const val FILE_ID: String = "A1234B"
    }

    private val filesDAO: TestFilesDAO = TestFilesDAO()

    @Test
    fun findFile_found() {
        val savedFile: File = TestFile().new()
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, savedFile)
                .instance
        )

        val result: File = boraService.findFile(FILE_ID)
        assert(result == savedFile)
    }

    @Test(expected = FileNotFoundException::class)
    fun findFile_not_found() {
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, null)
                .instance
        )

        boraService.findFile(FILE_ID)
    }
}