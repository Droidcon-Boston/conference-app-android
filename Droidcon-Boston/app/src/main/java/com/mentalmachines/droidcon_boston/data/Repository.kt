package com.mentalmachines.droidcon_boston.data

import android.content.Context
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia

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

    fun getTweets(): List<TweetWithMedia> {
        return remoteDataSource.getTweets()
    }

    fun getTweetsFromDB(): List<TweetWithMedia> {
        return localDataSource.getTweets()
    }

    fun updateDBTweets(tweets: List<TweetWithMedia>) {
        localDataSource.updateTweets(tweets)
    }
}
