package com.mentalmachines.droidcon_boston.data

import android.content.Context
import com.mentalmachines.droidcon_boston.modal.Tweet

class Repository private constructor(private val remoteDataSource: RemoteDataSource,
                                     private val localDataSource: LocalDataSource) {

    companion object {

        private lateinit var repository: Repository

        fun getInstance(context: Context): Repository {
            if (!::repository.isInitialized) {
                repository = Repository(RemoteDataSource.getInstance(), LocalDataSource
                    .getInstance(context))
            }
            return repository
        }
    }

    fun getTweets(): List<Tweet> {
        return remoteDataSource.getTweets()
    }

    fun getTweetsFromDB(): List<Tweet> {
        return localDataSource.getTweets()
    }

    fun updateDBTweets(tweets: List<Tweet>) {
        localDataSource.updateTweets(tweets)
    }
}
