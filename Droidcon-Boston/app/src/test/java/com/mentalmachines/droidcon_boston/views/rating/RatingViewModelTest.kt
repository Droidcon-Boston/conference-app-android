package com.mentalmachines.droidcon_boston.views.rating

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class RatingViewModelTest {
    private val viewModel = RatingViewModel()

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