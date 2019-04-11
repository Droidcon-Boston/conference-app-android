package com.mentalmachines.droidcon_boston.domain

import com.mentalmachines.droidcon_boston.data.Repository
import com.mentalmachines.droidcon_boston.modal.TweetWithMedia
import java.lang.Exception

class TwitterUseCase(private val repository: Repository) : UseCase<Unit, List<TweetWithMedia>>() {

    override fun execute(parameters: Unit): List<TweetWithMedia> {
        val tweets = try {
            repository.getTweets().run {
                if (isEmpty()) {
                    repository.getTweetsFromDB()
                } else {
                    taskScheduler.execute { repository.updateDBTweets(this) }
                    this
                }
            }
        } catch (e: Exception) {
            repository.getTweetsFromDB()
        }
        if (tweets.isEmpty()) {
            throw Exception("Empty list")
        }
        return tweets
    }
}
