package com.mentalmachines.droidcon_boston.data

import androidx.lifecycle.LiveData
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.Tweet

interface DataSource {
    fun getTweets(): LiveData<Result<List<Tweet>>>
    fun refreshTweets()
}