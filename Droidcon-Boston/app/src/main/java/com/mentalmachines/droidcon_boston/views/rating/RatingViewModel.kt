package com.mentalmachines.droidcon_boston.views.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import timber.log.Timber

class RatingViewModel(
    private val firebaseHelper: FirebaseHelper = FirebaseHelper.instance
) : ViewModel() {

    private val feedbackSent = MutableLiveData<Boolean>()

    private lateinit var userId: String
    private lateinit var ratingRepo: RatingRepo

    fun getFeedbackSent(): LiveData<Boolean> = feedbackSent

    fun handleSubmission(rating: Int, feedback: String, sessionId: String) {
        val sessionFeedback = SessionFeedback(rating, feedback, sessionId)

        submitFeedback(sessionFeedback)
    }

    /**
     * Posts the session feedback to the server.
     */
    private fun submitFeedback(sessionFeedback: SessionFeedback) {
        ratingRepo.setSessionFeedback(sessionFeedback.sessionId, sessionFeedback)

        Timber.d("Submitting feedback: $sessionFeedback")
        feedbackSent.value = true
    }

    fun init(userId: String, ratingRepo: RatingRepo) {
        this.userId = userId
        this.ratingRepo = ratingRepo
    }
}
