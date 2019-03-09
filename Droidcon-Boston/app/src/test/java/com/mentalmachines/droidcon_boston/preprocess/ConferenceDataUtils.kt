package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap

class ConferenceDataUtils {
    companion object {

        fun getMoshiInstance(): Moshi {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .add(Duration::class.java, IsoDurationJsonAdapter().nullSafe())
                    .build()
            return moshi
        }

        fun processConferenceData(confData: ConferenceDataModel?, errorsFatal: Boolean = true) {
            denormalizeConferenceData(confData, errorsFatal)
            fixSpeakerNames(confData)
        }

        fun denormalizeConferenceData(confData: ConferenceDataModel?, errorsFatal: Boolean = true) {
            confData?.events?.forEach {
                // denormalize speakers
                val speakerNames = HashMap<String, Boolean>()
                val speakerNameToPhotoUrl = HashMap<String, String>()
                val speakerNameToOrg = HashMap<String, String>()
                it.value.speakerIds?.forEach {
                    confData.speakers.get(it.key)?.let {
                        speakerNames.put(it.name, true)
                        if (it.pictureUrl != null) {
                            speakerNameToPhotoUrl.put(it.name, it.pictureUrl)
                        }
                        if (it.org != null) {
                            speakerNameToOrg.put(it.name, it.org)
                        }
                    }
                }
                if (speakerNames.size > 0) {
                    it.value.speakerNames = speakerNames
                    it.value.speakerNameToPhotoUrl = speakerNameToPhotoUrl
                    it.value.speakerNameToOrg = speakerNameToOrg

                    if (speakerNames.size == 1) {
                        it.value.primarySpeakerName = speakerNames.entries.iterator().next().key
                    } else {
                        if (speakerNames.containsKey("Giorgio Natili")) {
                            it.value.primarySpeakerName = "Giorgio Natili"
                        } else if (speakerNames.containsKey("Kaan Mamikoglu")) {
                            it.value.primarySpeakerName = "Kaan Mamikoglu"
                        } else if (speakerNames.containsKey("Adrián Catalan")) {
                            it.value.primarySpeakerName = "Adrián Catalan"
                        } else if (speakerNames.containsKey("Oleg Golberg")) {
                            it.value.primarySpeakerName = "Oleg Golberg"
                        } else if (errorsFatal) {
                            throw IllegalStateException("Didn't handle case of speakernames: " + speakerNames)
                        }
                    }
                }
                // denormalize rooms
                val roomNames = HashMap<String, Boolean>()
                it.value.roomIds?.forEach {
                    confData.rooms.get(it.key)?.let {
                        roomNames.put(it.name, true)
                    }
                }
                if (roomNames.size > 0) {
                    it.value.roomNames = roomNames
                }
                // look up track name
                it.value.trackId?.apply {
                    val trackInfo = confData.tracks.get(it.value.trackId!!)
                    trackInfo?.let { track ->
                        it.value.trackName = track.name
                        it.value.trackSortOrder = track.sortOrder
                    }
                }
                // calculate the end time
                it.value.endTime = Date.from(it.value.startTime.toInstant().plus(it.value.duration))
            }
        }

        fun fixSpeakerNames(confData: ConferenceDataModel?) {
            confData?.speakers?.forEach {
                val speaker = it.value
                val splitName = speaker.name.split(' ')
                speaker.firstName = splitName.first()
                if (splitName.size > 1) {
                    speaker.lastName = splitName.last()
                }
            }
        }

        fun getSlidesupDataFromAPI(baseUrl: String): String {
            val client = OkHttpClient()
            val sections = getUrlRawData(client, "$baseUrl/sections.json?print=pretty")
            val tracks = getUrlRawData(client, "$baseUrl/tracks.json?print=pretty")
            val events = getUrlRawData(client, "$baseUrl/events.json?print=pretty")
            val speakers = getUrlRawData(client, "$baseUrl/speakers.json?print=pretty")
            val rooms = getUrlRawData(client, "$baseUrl/rooms.json?print=pretty")
            val confExportJson = "{\n" +
                    "  \"sections\" : $sections,\n" +
                    "  \"tracks\" : $tracks,\n" +
                    "  \"events\" : $events,\n" +
                    "  \"speakers\" : $speakers,\n" +
                    "  \"rooms\" : $rooms\n}"
            return confExportJson
        }

        @Throws(IOException::class)
        fun getUrlRawData(okHttpClient: OkHttpClient, url: String): String? {
            val request = Request.Builder()
                .url(url)
                .build()

            okHttpClient.newCall(request).execute().use { response -> return response.body()?.string() }
        }
    }
}