package com.mentalmachines.droidcon_boston.views.rating

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.mockito.Mockito.mock

class RatingViewModelTest {
    private val mockFirebase = mock(FirebaseHelper::class.java)

    private val viewModel = RatingViewModel(mockFirebase)

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