package com.mentalmachines.droidcon_boston.data

import androidx.lifecycle.LiveData
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.Tweet


//TODO: Add local data source
class Repository private constructor(private val remoteDataSource: DataSource) {

    companion object {

        private var repository: Repository? = null

        fun getInstance(): Repository {
            if (repository == null) {
                repository = Repository(RemoteDataSource.getInstance())
            }
            return repository!!
        }
    }


    fun getTweets(): LiveData<Result<List<Tweet>>> {
        return remoteDataSource.getTweets()
    }

    fun refreshTweets() = remoteDataSource.refreshTweets()

}