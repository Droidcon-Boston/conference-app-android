package com.mentalmachines.droidcon_boston.views.rating

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class RatingRepo(
    var userId: String,
    private val userDatabase: DatabaseReference
) {
    fun getSessionFeedback(sessionId: String, feedbackCallback: (SessionFeedback?) -> Unit) {
        userDatabase.child(getSessionFeedbackPath(sessionId))
            .addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.e(error.toException())
                    feedbackCallback(null)
                }

                override fun onDataChange(data: DataSnapshot) {
                    var rating: Int = 0
                    var comments: String = ""
                    data.children.forEach {
                        when (it.key) {
                            "rating" -> rating = (it.value as Long).toInt()
                            "feedback" -> comments = it.value as String
                        }
                    }
                    feedbackCallback(SessionFeedback(rating, comments))
                }

            })
    }

    fun setSessionFeedback(sessionId: String, sessionRating: SessionFeedback) {
        userDatabase.child(getSessionFeedbackPath(sessionId))
            .setValue(sessionRating)
    }

    private fun getSessionFeedbackPath(sessionId: String): String {
        return "$userId/sessionFeedback/$sessionId"
    }
}
