package com.mentalmachines.droidcon_boston.preprocess

import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.denormalizeConferenceData
import com.mentalmachines.droidcon_boston.preprocess.ConferenceDataUtils.Companion.getMoshiInstance
import okio.Okio
import org.junit.Test

class ConferenceDataTests {

    @Test
    fun testReadConfData() {
        val datastream = this.javaClass.classLoader.getResourceAsStream("sample-conference-export.json")

        val moshi = getMoshiInstance()
        val jsonAdapter = moshi.adapter(ConferenceDataModel::class.java)
        val confData = jsonAdapter.fromJson(Okio.buffer(Okio.source(datastream)))

        assert(confData?.events?.size ?: 0 > 0)
        assert(confData?.rooms?.size ?: 0 > 0)
        assert(confData?.sections?.size ?: 0 > 0)
        assert(confData?.speakers?.size ?: 0 > 0)
        assert(confData?.tracks?.size ?: 0 > 0)

        denormalizeConferenceData(confData, false)

        confData?.events?.forEach {
            assert(it.value.speakerNames?.size ?: 0 > 0)
        }
    }
}
