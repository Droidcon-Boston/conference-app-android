package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class VolunteerDataUtils {
    companion object {

        fun getMoshiInstance(): Moshi {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val parameterizedType = Types.newParameterizedType(List::class.java, VolunteerModel::class.java);
            return moshi
        }

        fun getCsvVolunteerAdapter() : JsonAdapter<List<CsvVolunteerModel>> {
            val moshi = getMoshiInstance()
            val parameterizedType = Types.newParameterizedType(List::class.java, CsvVolunteerModel::class.java)
            val csvJsonAdapter = moshi.adapter<List<CsvVolunteerModel>>(parameterizedType)
            return csvJsonAdapter
        }

        fun getVolunteerAdapter() : JsonAdapter<Map<Integer, VolunteerModel>> {
            val moshi = getMoshiInstance()
            val parameterizedType = Types.newParameterizedType(Map::class.java, Integer::class.java, VolunteerModel::class.java)
            val csvJsonAdapter = moshi.adapter<Map<Integer, VolunteerModel>>(parameterizedType)
            return csvJsonAdapter
        }

        fun processData(data: List<CsvVolunteerModel>?): Map<Integer, VolunteerModel> {
            val volunteerModels = HashMap<Integer, VolunteerModel>()
            var volunteerIndex = 0
            data?.forEach {
                val volunteerModel = VolunteerModel(it)
                volunteerModels.put(Integer(volunteerIndex++), volunteerModel)
            }

            return volunteerModels
        }
    }
}