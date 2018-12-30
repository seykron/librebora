package net.borak.domain.bora

import kotlinx.coroutines.*
import net.borak.domain.bora.model.SectionListRequest
import net.borak.domain.bora.model.SectionPage
import org.joda.time.DateTime
import org.joda.time.Duration

class SectionImporter(private val boraClient: BoraClient) {

    companion object {
        private const val DAYS_BATCH_SIZE: Int = 4
    }

    fun import(sectionName: String,
               startDate: DateTime,
               endDate: DateTime): List<SectionPage> = runBlocking {

        val job = Job()
        val days: Int = Duration(startDate, endDate).standardDays.toInt()

        (0..days step DAYS_BATCH_SIZE).map { day ->
            val lastDay: Int = if (day + DAYS_BATCH_SIZE < days) {
                day + DAYS_BATCH_SIZE
            } else {
                days
            }

            listSection(
                job = job,
                sectionName = sectionName,
                startDate = startDate,
                days = day..lastDay
            ).flatMap { futurePages ->
                futurePages.await()
            }
        }.flatten()
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