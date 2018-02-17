package com.mentalmachines.droidcon_boston.data;

import android.content.Context
import android.content.res.AssetManager
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import java.nio.charset.Charset
import java.util.Date
import java.util.HashMap

class ConferenceDataUtils {

    companion object {

        fun getMoshiInstance(): Moshi {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                    .build()
            return moshi
        }

        fun denormalizeConferenceData(confData: ConferenceDataModel?) {
            confData?.events?.forEach {
                // denormalize speakers
                val speakerNames = HashMap<String, Boolean>()
                it.value.speakerIds?.forEach {
                    confData.speakers.get(it.key)?.let {
                        speakerNames.put(it.name, true)
                    }
                }
                if (speakerNames.size > 0) {
                    it.value.speakerNames = speakerNames
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

        fun AssetManager.fileAsString(subdirectory: String, filename: String): String {
            return open("$subdirectory/$filename").use {
                it.readBytes().toString(Charset.defaultCharset())
            }
        }

        fun parseData (ctx: Context) : ConferenceDataModel? {
            val inputJson = ctx.assets.fileAsString("databases", "sample_data.json")
            //Log.d("json", "read file? " + inputJson)
            val moshi = ConferenceDataUtils.getMoshiInstance()
            val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
            val confData = jsonAdapter.fromJson(inputJson)

            ConferenceDataUtils.denormalizeConferenceData(confData)
            return confData
        }
    }
}