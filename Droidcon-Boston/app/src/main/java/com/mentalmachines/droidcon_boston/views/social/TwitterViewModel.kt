package com.mentalmachines.droidcon_boston.views.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mentalmachines.droidcon_boston.data.Repository
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.Tweet

class TwitterViewModel : ViewModel() {

    val tweets: LiveData<Result<List<Tweet>>> by lazy {
        Repository.getInstance().getTweets()
    }

    fun refreshTweets(){
        return Repository.getInstance().refreshTweets()
    }
}
