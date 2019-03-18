package com.mentalmachines.droidcon_boston.data

import android.content.Context
import com.mentalmachines.droidcon_boston.modal.Tweet

class LocalDataSource private constructor(private val appDatabase: AppDatabase) : DataSource {

    companion object {
        private lateinit var dataSource: LocalDataSource
        fun getInstance(context: Context): LocalDataSource {
            if (!::dataSource.isInitialized) {
                dataSource = LocalDataSource(AppDatabase.getInstance(context))
            }
            return dataSource
        }
    }

    override fun getTweets(): List<Tweet> {
        return appDatabase.twitterDao().getTweets()
    }

    fun updateTweets(tweets: List<Tweet>) {
        appDatabase.twitterDao().updateTweets(tweets)
    }
}
