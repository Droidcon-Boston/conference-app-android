#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0
//INCLUDE ConferenceDataModels.kt
//INCLUDE ConferenceDataUtils.kt
//INCLUDE IsoDurationJsonAdapter.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY Denormalize

import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataModel
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils
import okio.Okio
import java.io.FileInputStream

class Denormalize {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: denormSlidesUp <jsonfile>")
                kotlin.system.exitProcess(-1)
            }
            val inputStream = FileInputStream(args.get(0))

            val moshi = ConferenceDataUtils.getMoshiInstance()
            val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
            val confData = jsonAdapter.fromJson(Okio.buffer(Okio.source(inputStream)))

            ConferenceDataUtils.processConferenceData(confData)

            val denormalizedJson = jsonAdapter.toJson(confData)
            println(denormalizedJson)

        }
    }
}