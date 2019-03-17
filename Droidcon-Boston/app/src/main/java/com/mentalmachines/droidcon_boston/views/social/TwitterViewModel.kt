package com.mentalmachines.droidcon_boston.views.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentalmachines.droidcon_boston.domain.TwitterUseCase
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.Tweet

class TwitterViewModel(private val twitterUseCase: TwitterUseCase) : ViewModel() {

    private val tweetLiveData: MutableLiveData<Result<List<Tweet>>> by lazy {
        MutableLiveData<Result<List<Tweet>>>()
    }

    val tweets: LiveData<Result<List<Tweet>>> by lazy {
        twitterUseCase(Unit, tweetLiveData)
    }

    fun refreshTweets() {
        twitterUseCase(Unit, tweetLiveData)
    }
}
