package net.librebora.application.model

import net.librebora.domain.model.Cursor
import net.librebora.domain.model.File

/** Factory to create [FileDTO]s.
 * @property paginatedResultFactory Factory to create paginated results.
 */
class FilesFactory(private val paginatedResultFactory: PaginatedResultFactory) {

    /** Creates a paginated result of files.
     *
     * @param servicePath Current service path.
     * @param cursor Pagination info.
     * @param files Files to convert.
     */
    fun createFiles(
        servicePath: String,
        cursor: Cursor,
        files: List<File>
    ): PaginatedResult<FileDTO> {
        return paginatedResultFactory.create(
            servicePath = servicePath,
            cursor = cursor,
            items = files.map(this::createFile)
        )
    }

    /** Creates a [FileDTO].
     * @param file File to convert to [FileDTO].
     */
    fun createFile(file: File): FileDTO {
        return FileDTO(
            id = file.id.toString(),
            section = file.section,
            fileId = file.fileId,
            categoryId = file.categoryId,
            categoryName = file.categoryName,
            publicationDate = file.publicationDate,
            text = file.text,
            pdfFile = file.pdfFile
        )
    }
}