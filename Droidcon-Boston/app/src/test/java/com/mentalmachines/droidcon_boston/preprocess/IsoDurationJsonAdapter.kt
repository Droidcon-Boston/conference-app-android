package com.mentalmachines.droidcon_boston.preprocess

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import java.io.IOException
import java.time.Duration

class IsoDurationJsonAdapter : JsonAdapter<Duration>() {
    @Synchronized
    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): Duration? {
        val string = reader.nextString()
        return Duration.parse(string)
    }

    @Synchronized
    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: Duration?) {
        writer.value(value?.toString())
    }
}
