package com.mentalmachines.droidcon_boston.preprocess

import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.denormalizeConferenceData
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.getMoshiInstance
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import org.junit.Test
import java.io.IOException
import java.io.InputStream


class ConferenceDataTests {

    @Test
    fun testReadConfData() {
        val classLoader = this.javaClass.classLoader!!
        val dataStream = classLoader.getResourceAsStream("sample-conference-export.json")

        verifyConfData(dataStream)
    }

    @Test
    fun testDownloadConfData() {
        val client = OkHttpClient()
        val baseUrl = "https://slidesup-test.firebaseio.com/confs/detail/boston-conference-2018"
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

        val dataStream = confExportJson.byteInputStream()

        verifyConfData(dataStream)
    }

    @Throws(IOException::class)
    fun getUrlRawData(okHttpClient: OkHttpClient, url: String): String? {
        val request = Request.Builder()
            .url(url)
            .build()

        okHttpClient.newCall(request).execute().use { response -> return response.body()?.string() }
    }

    private fun verifyConfData(dataStream: InputStream?) {
        val moshi = getMoshiInstance()
        val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
        val confData = jsonAdapter.fromJson(Okio.buffer(Okio.source(dataStream)))

        assert(confData?.events?.size ?: 0 > 0)
        assert(confData?.rooms?.size ?: 0 > 0)
        assert(confData?.sections?.size ?: 0 > 0)
        assert(confData?.speakers?.size ?: 0 > 0)
        assert(confData?.tracks?.size ?: 0 > 0)

        denormalizeConferenceData(confData, false)

        confData?.events?.forEach {
            assert(it.value.name.isNotBlank())
        }
    }
}
