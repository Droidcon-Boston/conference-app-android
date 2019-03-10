package com.mentalmachines.droidcon_boston.data

import androidx.lifecycle.LiveData
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.Tweet


//TODO : Except room instance
class LocalDataSource private constructor() : DataSource {

    companion object {
        private lateinit var dataSource: DataSource

        fun getInstance(): DataSource {
            if (!::dataSource.isInitialized) {
                dataSource = LocalDataSource()
            }
            return dataSource
        }

    }

    override fun getTweets(): LiveData<Result<List<Tweet>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refreshTweets() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}