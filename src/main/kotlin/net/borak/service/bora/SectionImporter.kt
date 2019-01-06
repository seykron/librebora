package net.borak.service.bora

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.borak.service.bora.model.ImportTask
import net.borak.service.bora.model.SectionFile
import net.borak.service.bora.model.SectionListRequest
import net.borak.service.bora.model.SectionPage
import org.joda.time.DateTime

class SectionImporter(private val boraClient: BoraClient) {

    fun importPages(tasks: List<ImportTask>,
                    taskFinishCallback: (ImportTask, List<SectionPage>) -> Unit): Unit = runBlocking {
        val job = Job()

        tasks.forEach { importTask ->
            val sectionPages: List<SectionPage> = listSection(
                job = job,
                sectionName = importTask.sectionName,
                startDate = importTask.startDate,
                days = importTask.dayStart..importTask.dayEnd
            ).flatMap { futurePages ->
                futurePages.await()
            }

            taskFinishCallback(importTask, sectionPages)
        }
    }

    fun importFiles(sectionName: String,
                    sectionPage: SectionPage): List<SectionFile> {
        return sectionPage.items.map { sectionItem ->
            boraClient.retrieve(sectionName, sectionItem.fileId)
        }
    }

    private fun listSection(job: Job,
                            sectionName: String,
                            startDate: DateTime,
                            days: IntRange): List<Deferred<List<SectionPage>>> = runBlocking {

        days.map { day ->
            async(context = job) {
                listSection(SectionListRequest.create(
                    sectionName = sectionName,
                    offset = 1,
                    itemsPerPage = 500,
                    date = startDate.plusDays(day)
                ))
            }
        }
    }

    private fun listSection(request: SectionListRequest): List<SectionPage> {
        return listOf(boraClient.list(request))
    }
}