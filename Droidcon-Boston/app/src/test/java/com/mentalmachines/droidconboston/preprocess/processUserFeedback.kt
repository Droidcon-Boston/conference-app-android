#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0,com.squareup.okhttp3:okhttp:3.13.1,com.google.firebase:firebase-admin:6.7.0
//INCLUDE ConferenceDataModels.kt
//INCLUDE ConferenceDataUtils.kt
//INCLUDE IsoDurationJsonAdapter.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY ProcessUserFeedback

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataModel
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils
import com.mentalmachines.droidcon_boston.preprocess.UserModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Types
import okio.Okio
import java.io.FileInputStream
import java.util.concurrent.CountDownLatch

class ProcessUserFeedback {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 2) {
                System.err.println("Usage: processUserFeedback <firebase_jsonfile> " +
                        "<users_export_json>")
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

            val moshi = ConferenceDataUtils.getMoshiInstance()
            val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
            val confData = jsonAdapter.fromJson(Okio.buffer(Okio.source(data.byteInputStream())))

            ConferenceDataUtils.processConferenceData(confData)

            val userDataType = Types.newParameterizedType(Map::class.java, String::class.java,
                UserModel::class.java)
            val jsonUserAdapter: JsonAdapter<Map<String, UserModel>> = moshi.adapter(userDataType)
            val userData = jsonUserAdapter.fromJson(JsonReader.of(Okio.buffer(Okio.source
                (FileInputStream(args[1])))))

            val usersWithFeedback = userData?.filter { it.value.sessionFeedback != null }

            usersWithFeedback?.forEach { userId, userData ->
                userData.sessionFeedback?.forEach { sessionId, userFeedback ->
                    println("${sessionId}\t${confData?.events?.get(sessionId)
                        ?.name}\t${userId}\t${userData
                        .displayName}\t${userFeedback
                        .rating}\t${userFeedback
                        .feedback}")
                }
            }
        }
    }
}
