package net.borak.cli.command

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import net.borak.cli.model.DataSets
import net.borak.domain.ProfileService
import net.borak.domain.model.Profile
import net.borak.support.CSVParser
import org.joda.time.DateTime
import java.io.File

class LoadDataCommand(private val profileService: ProfileService) : CliktCommand(
    help = "Loads data models from CSV into the database.",
    name = "loadData"
) {

    private val file: File by option("-f", "--file").file(exists = true).required()
    private val datasetName: String by option("-d", "--dataset").required()

    private val loaders: Map<DataSets, () -> Unit> = mapOf(
        DataSets.PROFILES to this::loadProfiles
    )

    override fun run() {
        val dataSet: DataSets = DataSets.valueOf(datasetName.toUpperCase())

        if (loaders.containsKey(dataSet)) {
            loaders.getValue(dataSet)()
        } else {
            echo("Data set not supported $datasetName")
        }
    }

    private fun loadProfiles() {
        var header = true
        val profiles: MutableList<Profile> = mutableListOf()

        echo("${DateTime.now()} delete all profiles from DB")

        profileService.deleteAll()

        echo("${DateTime.now()} parsing csv and inserting new records")

        CSVParser().parse(file.bufferedReader()) { record ->
            if (!header) {
                if (record.size >= 20) {
                    val profile = Profile.new(
                        firstName = record[19],
                        lastName = record[0],
                        documentId = record[7],
                        address = record[5],
                        age = record[8].toInt(),
                        birthday = null,
                        gender = record[25],
                        email = record[9]
                    )

                    profiles.add(profile)
                }
            }
            header = false
        }

        echo("${DateTime.now()} parsing complete, saving data")
        profileService.batchInsert(profiles)
    }
}
