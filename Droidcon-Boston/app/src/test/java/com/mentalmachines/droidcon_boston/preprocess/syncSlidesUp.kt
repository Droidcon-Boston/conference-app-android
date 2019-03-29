#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0,com.squareup.okhttp3:okhttp:3.13.1,com.google.firebase:firebase-admin:6.7.0
//INCLUDE ConferenceDataModels.kt
//INCLUDE ConferenceDataUtils.kt
//INCLUDE IsoDurationJsonAdapter.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY Sync

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataModel
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils
import okio.Okio
import java.io.FileInputStream
import java.util.concurrent.CountDownLatch

class Sync {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: syncSlidesUp <firebase_jsonfile>")
                kotlin.system.exitProcess(-1)
            }
            val inputStream = FileInputStream(args[0])
            val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://droidcon-bos-2019.firebaseio.com/")
                .build()
            FirebaseApp.initializeApp(options)

            val data = ConferenceDataUtils.getSlidesupDataFromAPI("https://slidesup-8b9d6" +
                    ".firebaseio.com/confs/detail/droidcon-boston-2019")
            println(data)
            println("==========")

            val moshi = ConferenceDataUtils.getMoshiInstance()
            val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
            val confData = jsonAdapter.fromJson(Okio.buffer(Okio.source(data.byteInputStream())))

            ConferenceDataUtils.processConferenceData(confData)
            val denormalizedJson = jsonAdapter.toJson(confData)
            println(denormalizedJson)

            val adapter = moshi.adapter(Any::class.java)
            val jsonStructure = adapter.toJsonValue(confData)
            @Suppress("UNCHECKED_CAST")
            val jsonObject = jsonStructure as Map<String, Any>

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("conferenceData")
            
            val latch = CountDownLatch(1)
            ref.setValue(jsonObject) { error, _ ->
                error?.let {
                    println("Error: {$it.message}")
                }
                latch.countDown()
            }
            latch.await()
        }
    }
}
