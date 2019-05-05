package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class FaqDataUtils {
    companion object {

        fun getMoshiInstance(): Moshi {
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            val parameterizedType = Types.newParameterizedType(List::class.java, CsvFaqModel::class.java);
            return moshi
        }

        fun getCsvFaqAdapter() : JsonAdapter<Map<String, List<CsvFaqModel>>> {
            val moshi = getMoshiInstance()
            val innerParameterizedType = Types.newParameterizedType(List::class.java, CsvFaqModel::class.java)
            val parameterizedType = Types.newParameterizedType(Map::class.java, String::class.java, innerParameterizedType)
            val csvJsonAdapter = moshi.adapter<Map<String, List<CsvFaqModel>>>(parameterizedType)
            return csvJsonAdapter
        }

        fun getFaqAdapter() : JsonAdapter<Map<Integer, FaqsModel>> {
            val moshi = getMoshiInstance()
            val parameterizedType = Types.newParameterizedType(Map::class.java, Integer::class.java, FaqsModel::class.java)
            val csvJsonAdapter = moshi.adapter<Map<Integer, FaqsModel>>(parameterizedType)
            return csvJsonAdapter
        }

        fun processFaqData(faqData: Map<String, List<CsvFaqModel>>?): Map<Integer, FaqsModel> {
            val faqsModels = HashMap<Integer, FaqsModel>()
            var faqIndex = 0
            faqData?.entries?.forEach {
                val orderedFaqModels = HashMap<Integer, FaqModel>()
                var answerIndex = 0
                it.value.forEach { csvModel ->
                    val faqModel = FaqModel(csvModel)
                    orderedFaqModels.put(Integer(answerIndex++), faqModel)
                }
                val faqsModel = FaqsModel(it.key, orderedFaqModels)
                faqsModels.put(Integer(faqIndex++), faqsModel)
            }

            return faqsModels
        }
    }
}