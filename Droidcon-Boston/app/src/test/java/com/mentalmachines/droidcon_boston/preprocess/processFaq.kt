#!/usr/bin/env kscript
//DEPS com.squareup.moshi:moshi:1.5.0,com.squareup.moshi:moshi-adapters:1.5.0,com.squareup.moshi:moshi-kotlin:1.5.0
//INCLUDE FaqDataModels.kt
//INCLUDE FaqDataUtils.kt
// note: the @file notation for the above doesn't seem to work yet :-P

//ENTRY Faq

import com.mentalmachines.droidcon_boston.preprocess.FaqDataUtils
import okio.Okio
import java.io.FileInputStream

class Faq {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                System.err.println("Usage: processFaq <jsonfile>")
                kotlin.system.exitProcess(-1)
            }
            val inputStream = FileInputStream(args.get(0))

            val csvJsonAdapter = FaqDataUtils.getCsvFaqAdapter()
            val csvFaqData = csvJsonAdapter.fromJson(Okio.buffer(Okio.source(inputStream)))

            val faqData = FaqDataUtils.processFaqData(csvFaqData)

            val jsonAdapter = FaqDataUtils.getFaqAdapter()
            val fixedJson = jsonAdapter.toJson(faqData)
            println(fixedJson)

        }
    }
}
