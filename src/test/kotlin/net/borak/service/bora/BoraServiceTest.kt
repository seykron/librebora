package net.borak.service.bora

import net.borak.domain.model.File
import net.borak.domain.model.FileNotFoundException
import net.borak.util.mock.TestBoraClient
import net.borak.util.mock.TestFile
import net.borak.util.mock.TestFilesDAO
import net.borak.util.mock.TestSectionFile
import org.junit.Test

class BoraServiceTest {

    companion object {
        private const val FILE_ID: String = "A1234B"
    }

    private val filesDAO: TestFilesDAO = TestFilesDAO()
    private val boraClient: TestBoraClient = TestBoraClient()

    @Test
    fun importFile_not_found() {
        val savedFile: File = TestFile().new()
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, null)
                .saveOrUpdate(savedFile)
                .instance,
            boraClient = boraClient
                .retrieve("segunda", FILE_ID, TestSectionFile(id = FILE_ID).new())
                .instance
        )

        val result: File = boraService.importFile("segunda", FILE_ID)
        assert(result == savedFile)
    }

    @Test
    fun importFile_exists() {
        val savedFile: File = TestFile().new()
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, savedFile)
                .instance,
            boraClient = boraClient.instance
        )

        val result: File = boraService.importFile("segunda", FILE_ID)
        assert(result == savedFile)
    }

    @Test
    fun findFile_found() {
        val savedFile: File = TestFile().new()
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, savedFile)
                .instance,
            boraClient = boraClient.instance
        )

        val result: File = boraService.findFile(FILE_ID)
        assert(result == savedFile)
    }

    @Test(expected = FileNotFoundException::class)
    fun findFile_not_found() {
        val boraService = BoraService(
            filesDAO = filesDAO
                .findFile(FILE_ID, null)
                .instance,
            boraClient = boraClient.instance
        )

        boraService.findFile(FILE_ID)
    }
}