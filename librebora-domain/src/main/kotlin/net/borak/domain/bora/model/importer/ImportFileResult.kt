package net.borak.domain.bora.model.importer

import net.borak.domain.bora.model.sections.SectionFile

data class ImportFileResult(val fileId: String,
                            val sectionFile: SectionFile?,
                            val error: String?) {
    companion object {

        fun success(fileId: String,
                    sectionFile: SectionFile): ImportFileResult {
            return ImportFileResult(
                fileId = fileId,
                sectionFile = sectionFile,
                error = null
            )
        }

        fun error(fileId: String,
                  error: String): ImportFileResult {
            return ImportFileResult(
                fileId = fileId,
                sectionFile = null,
                error = error
            )
        }
    }

    fun isSuccess(): Boolean {
        return sectionFile != null
    }

    fun isError(): Boolean {
        return sectionFile == null
    }
}
