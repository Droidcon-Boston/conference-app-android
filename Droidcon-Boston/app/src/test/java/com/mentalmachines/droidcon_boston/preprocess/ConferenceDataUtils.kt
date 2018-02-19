package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import java.util.*
import kotlin.collections.HashMap

class ConferenceDataUtils {
    companion object {

        fun getMoshiInstance(): Moshi {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .build()
            return moshi
        }

        fun processConferenceData(confData: ConferenceDataModel?) {
            denormalizeConferenceData(confData)
            fixSpeakerNames(confData)
        }

        fun denormalizeConferenceData(confData: ConferenceDataModel?) {
            confData?.events?.forEach {
                // denormalize speakers
                val speakerNames = HashMap<String, Boolean>()
                val speakerNameToPhotoUrl = HashMap<String, String>()
                it.value.speakerIds?.forEach {
                    confData.speakers.get(it.key)?.let {
                        speakerNames.put(it.name, true)
                        if (it.pictureUrl != null) {
                            speakerNameToPhotoUrl.put(it.name, it.pictureUrl)
                        }
                    }
                }
                if (speakerNames.size > 0) {
                    it.value.speakerNames = speakerNames
                    it.value.speakerNameToPhotoUrl = speakerNameToPhotoUrl
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
                    it.value.trackName = confData.tracks.get(it.value.trackId!!)?.name
                }
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
    }
}