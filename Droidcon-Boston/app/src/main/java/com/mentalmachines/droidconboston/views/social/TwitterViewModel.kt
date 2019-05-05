package com.mentalmachines.droidconboston.views.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentalmachines.droidconboston.domain.TwitterUseCase
import com.mentalmachines.droidconboston.modal.Result
import com.mentalmachines.droidconboston.modal.TweetWithMedia

class TwitterViewModel(private val twitterUseCase: TwitterUseCase) : ViewModel() {

    private val tweetLiveData: MutableLiveData<Result<List<TweetWithMedia>>> by lazy {
        MutableLiveData<Result<List<TweetWithMedia>>>()
    }

    val tweets: LiveData<Result<List<TweetWithMedia>>> by lazy {
        twitterUseCase(Unit, tweetLiveData)
    }

    fun refreshTweets() {
        twitterUseCase(Unit, tweetLiveData)
    }
}
