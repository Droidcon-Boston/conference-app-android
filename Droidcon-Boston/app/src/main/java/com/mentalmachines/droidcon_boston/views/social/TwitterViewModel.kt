package com.mentalmachines.droidcon_boston.views.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentalmachines.droidcon_boston.domain.TwitterUseCase
import com.mentalmachines.droidcon_boston.modal.Result
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia

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
