package net.borak.domain.bora.persistence

import net.borak.domain.bora.model.ImportStatus
import net.borak.domain.bora.model.ImportTask
import net.borak.domain.bora.model.ImportTaskMetrics
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.UUIDTable
import org.joda.time.DateTime
import java.util.*

object ImportTasks : UUIDTable(name = "import_tasks") {
    val sectionName = varchar("section_name", 30)
    val date = datetime("date")
    val itemsPerPage = integer("items_per_page")
    val numberOfPages = integer("number_of_pages")
    val numberOfFiles = integer("number_of_files")
    val status = enumeration("status", ImportStatus::class)
}

class ImportTaskEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<ImportTaskEntity>(ImportTasks)
    var sectionName: String by ImportTasks.sectionName
    var date: DateTime by ImportTasks.date
    var itemsPerPage: Int by ImportTasks.itemsPerPage
    var numberOfPages: Int by ImportTasks.numberOfPages
    var numberOfFiles: Int by ImportTasks.numberOfFiles
    var status: ImportStatus by ImportTasks.status

    fun update(numberOfPages: Int,
               numberOfFiles: Int,
               status: ImportStatus): ImportTaskEntity {

        this.numberOfPages = numberOfPages
        this.numberOfFiles = numberOfFiles
        this.status = status

        return this
    }

    fun toImportTask(): ImportTask {
        return ImportTask(
            id = id.value,
            sectionName = sectionName,
            date = date,
            itemsPerPage = itemsPerPage,
            status = status,
            metrics = ImportTaskMetrics(
                numberOfPages = numberOfPages,
                numberOfFiles = numberOfFiles
            )
        )
    }
}
