package com.mentalmachines.droidconboston.domain

import com.mentalmachines.droidconboston.data.Repository
import com.mentalmachines.droidconboston.modal.TweetWithMedia
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
