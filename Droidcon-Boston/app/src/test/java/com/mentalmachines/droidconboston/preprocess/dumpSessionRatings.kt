#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0,com.squareup.okhttp3:okhttp:3.13.1,com.google.firebase:firebase-admin:6.7.0
//INCLUDE ConferenceDataModels.kt
//INCLUDE ConferenceDataUtils.kt
//INCLUDE IsoDurationJsonAdapter.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY DumpTsvRatings
// dumps session ratings as a tab separated values by session and then rating (multiple
// ratings per session) to stdout

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataModel
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils
import com.mentalmachines.droidcon_boston.preprocess.EventModel
import okio.Okio
import java.io.FileInputStream
import java.util.concurrent.CountDownLatch


data class EventInfoModel(
    val name: String
)

class DumpTsvRatings {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: dumpTsvRatings <firebase_jsonfile>")
                kotlin.system.exitProcess(-1)
            }
            val inputStream = FileInputStream(args[0])
            val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl("https://droidcon-bos-2019.firebaseio.com/")
                .build()
            FirebaseApp.initializeApp(options)

            val database = FirebaseDatabase.getInstance()
            val eventRef = database.getReference("conferenceData/events")

            val sessionLatch = CountDownLatch(1)
            eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError?) {
                    error(error.toString())
                }

                override fun onDataChange(dataSnapshot: DataSnapshot?) {
                    println("data received")
                    dataSnapshot?.let {
                        println("children count: ${it.childrenCount}")
                        for (snapshot in it.children) {
                            //NOTE: hangs at getValue...no idea why :-(
                            // use processUserFeedback instead
                            val event = snapshot.getValue(EventInfoModel::class.java)
                            println("${snapshot.key} - ${event.name}")
                        }
                    }
                    sessionLatch.countDown()
                }
            })
            sessionLatch.await()
        }
    }
}
