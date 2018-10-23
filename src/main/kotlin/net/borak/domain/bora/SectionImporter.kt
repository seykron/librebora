package net.borak.domain.bora

import kotlinx.coroutines.*
import net.borak.domain.bora.model.SectionListRequest
import net.borak.domain.bora.model.SectionPage
import org.joda.time.DateTime
import org.joda.time.Duration

class SectionImporter(private val boraClient: BoraClient) {

    fun import(sectionName: String,
               startDate: DateTime,
               endDate: DateTime): List<SectionPage> = runBlocking {

        val job = Job()
        val days: Int = Duration(startDate, endDate).standardDays.toInt()

        0.rangeTo(days).map { day ->
            async(context = job) {
                listSection(SectionListRequest.create(
                    sectionName = sectionName,
                    offset = 1,
                    itemsPerPage = 500,
                    date = startDate.plusDays(day)
                ))
            }
        }.flatMap { futurePage ->
            futurePage.await()
        }
    }

    private fun listSection(request: SectionListRequest): List<SectionPage> {
        return listOf(boraClient.list(request))
    }
}