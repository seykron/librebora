package net.librebora.connector.bora.persistence

import net.librebora.connector.bora.model.importer.ImportStatus
import net.librebora.connector.bora.model.importer.ImportSectionTask
import net.librebora.connector.bora.model.importer.ImportTaskMetrics
import net.librebora.support.persistence.AbstractEntity
import net.librebora.support.persistence.AbstractEntityClass
import org.jetbrains.exposed.dao.EntityID
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
    val errorMessage = varchar("error_message", 255).nullable()
}

class ImportTaskEntity(id: EntityID<UUID>) : AbstractEntity<ImportSectionTask>(id) {
    companion object : AbstractEntityClass<ImportSectionTask, ImportTaskEntity>(ImportTasks)
    var sectionName: String by ImportTasks.sectionName
    var date: DateTime by ImportTasks.date
    var itemsPerPage: Int by ImportTasks.itemsPerPage
    var numberOfPages: Int by ImportTasks.numberOfPages
    var numberOfFiles: Int by ImportTasks.numberOfFiles
    var status: ImportStatus by ImportTasks.status
    var errorMessage: String? by ImportTasks.errorMessage

    override fun create(source: ImportSectionTask): AbstractEntity<ImportSectionTask> {
        this.sectionName = source.sectionName
        this.date = source.date
        this.itemsPerPage = source.itemsPerPage
        return update(source)
    }

    override fun update(source: ImportSectionTask): ImportTaskEntity {

        this.numberOfPages = source.metrics.numberOfPages
        this.numberOfFiles = source.metrics.numberOfFiles
        this.status = source.status
        this.errorMessage = source.errorMessage

        return this
    }

    override fun toDomainType(): ImportSectionTask {
        return ImportSectionTask(
            id = id.value,
            sectionName = sectionName,
            date = date,
            itemsPerPage = itemsPerPage,
            status = status,
            errorMessage = errorMessage,
            metrics = ImportTaskMetrics(
                numberOfPages = numberOfPages,
                numberOfFiles = numberOfFiles
            )
        )
    }
}
