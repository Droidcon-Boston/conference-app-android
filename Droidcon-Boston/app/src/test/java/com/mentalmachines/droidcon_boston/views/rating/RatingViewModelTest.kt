package com.mentalmachines.droidcon_boston.views.rating

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.database.DatabaseReference
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock

class RatingViewModelTest {
    private val userDatabase = mock(DatabaseReference::class.java)
    private val viewModel = RatingViewModel().init("myUserID", RatingRepo("myUserId", userDatabase))

    @JvmField
    @Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Test
    fun handleSubmission() {
        val rating = 5
        val feedback = "Loved it!"
        val sessionId = "sessionId"

        viewModel.handleSubmission(rating, feedback, sessionId)
        assertTrue(viewModel.getFeedbackSent().value == true)
    }
}