package com.mentalmachines.droidcon_boston.preprocess

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
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
        val confExportJson = getSlidesupDataFromAPI(SLIDESUP_API_BASEURL)

        val dataStream = confExportJson.byteInputStream()

        verifyConfData(dataStream)
    }

    @Test
    fun testWriteFirebase() {
        val classLoader = this.javaClass.classLoader!!
        val serviceAccount = classLoader.getResourceAsStream("droidcon-bos-firebase-adminsdk-apses-5a4c189cb9.json")

        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://droidcon-bos-2019.firebaseio.com/")
            .build()
        FirebaseApp.initializeApp(options)

        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("chat")

        ref.setValueAsync(2.0)
    }

    private fun getSlidesupDataFromAPI(baseUrl: String): String {
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

    private fun verifyConfData(dataStream: InputStream) {
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

    companion object {
        private const val SLIDESUP_API_BASEURL = "https://slidesup-test.firebaseio.com/confs/detail/boston-conference-2018"
    }
}
