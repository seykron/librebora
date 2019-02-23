package net.borak.connector.bora.persistence

import net.borak.connector.bora.model.importer.ImportStatus
import net.borak.connector.bora.model.importer.ImportTask
import net.borak.connector.bora.model.importer.ImportTaskMetrics
import net.borak.support.persistence.AbstractEntity
import net.borak.support.persistence.AbstractEntityClass
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
}

class ImportTaskEntity(id: EntityID<UUID>) : AbstractEntity<ImportTask>(id) {
    companion object : AbstractEntityClass<ImportTask, ImportTaskEntity>(ImportTasks)
    var sectionName: String by ImportTasks.sectionName
    var date: DateTime by ImportTasks.date
    var itemsPerPage: Int by ImportTasks.itemsPerPage
    var numberOfPages: Int by ImportTasks.numberOfPages
    var numberOfFiles: Int by ImportTasks.numberOfFiles
    var status: ImportStatus by ImportTasks.status

    override fun create(source: ImportTask): AbstractEntity<ImportTask> {
        this.sectionName = source.sectionName
        this.date = source.date
        this.itemsPerPage = source.itemsPerPage
        return update(source)
    }

    override fun update(source: ImportTask): ImportTaskEntity {

        this.numberOfPages = source.metrics.numberOfPages
        this.numberOfFiles = source.metrics.numberOfFiles
        this.status = source.status

        return this
    }

    override fun toDomainType(): ImportTask {
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
