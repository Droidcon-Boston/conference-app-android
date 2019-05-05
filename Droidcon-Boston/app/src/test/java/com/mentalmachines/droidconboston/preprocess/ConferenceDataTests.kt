package com.mentalmachines.droidcon_boston.preprocess

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.denormalizeConferenceData
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.getMoshiInstance
import okio.Okio
import org.junit.Test
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
        val confExportJson = ConferenceDataUtils.getSlidesupDataFromAPI(SLIDESUP_API_BASEURL)

        val dataStream = confExportJson.byteInputStream()

        verifyConfData(dataStream)
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
        private const val SLIDESUP_API_BASEURL =
            "https://slidesup-8b9d6.firebaseio.com/confs/detail/droidcon-boston-2019"
    }
}
