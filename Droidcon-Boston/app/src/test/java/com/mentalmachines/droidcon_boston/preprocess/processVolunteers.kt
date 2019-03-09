#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0
//INCLUDE VolunteerDataModels.kt
//INCLUDE VolunteerDataUtils.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY Volunteers

import com.mentalmachines.droidcon_boston.preprocess.VolunteerDataUtils
import okio.Okio
import java.io.FileInputStream
import com.squareup.moshi.Types

class Volunteers {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: processVolunteers <jsonfile>")
                kotlin.system.exitProcess(-1)
            }
            val inputStream = FileInputStream(args.get(0))

            val csvJsonAdapter = VolunteerDataUtils.getCsvVolunteerAdapter()
            val csvData = csvJsonAdapter.fromJson(Okio.buffer(Okio.source(inputStream)))

            val data = VolunteerDataUtils.processData(csvData)

            val jsonAdapter = VolunteerDataUtils.getVolunteerAdapter()
            val fixedJson = jsonAdapter.toJson(data)
            println(fixedJson)

        }
    }
}