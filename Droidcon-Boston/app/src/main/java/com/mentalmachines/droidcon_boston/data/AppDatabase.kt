package com.mentalmachines.droidcon_boston.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mentalmachines.droidcon_boston.modal.Media
import com.mentalmachines.droidcon_boston.modal.QuotedTweet
import com.mentalmachines.droidcon_boston.modal.Tweet

@Database(entities = [Tweet::class, QuotedTweet::class, Media::class], version = 1, exportSchema = false)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "droidconbosapp-db"

        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java, DATABASE_NAME).build().also { INSTANCE = it }
        }
    }

    abstract fun tweetDao(): TweetDao
    abstract fun quotedTweetDao(): QuotedTweetDao
    abstract fun mediaDao(): MediaDao
}
