package com.mentalmachines.droidcon_boston.views.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.mentalmachines.droidcon_boston.R
import timber.log.Timber

class RatingDialog : DialogFragment() {
    private var sessionRatingBar: RatingBar? = null
    private var sessionFeedbackInput: TextInputEditText? = null
    private var cancelButton: Button? = null
    private var submitButton: Button? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_rating, container, false)

        findViews(view)

        setupClickListeners()

        return view
    }

    private fun findViews(view: View) {
        sessionRatingBar = view.findViewById(R.id.session_rating)
        sessionFeedbackInput = view.findViewById(R.id.session_feedback)
        cancelButton = view.findViewById(R.id.cancel)
        submitButton = view.findViewById(R.id.submit)
    }

    private fun setupClickListeners() {
        cancelButton?.setOnClickListener {
            this.dismiss()
        }

        submitButton?.setOnClickListener {
            val feedback = getFeedback()
            Timber.d("Submitting feedback: $feedback")
            this.dismiss()
        }

        sessionRatingBar?.setOnRatingBarChangeListener { _, rating, _ ->
            val enableSubmissions = rating > 0.0F
            submitButton?.isEnabled = enableSubmissions
        }
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }

    private fun getFeedback(): SessionFeedback {
        val rating = sessionRatingBar?.rating?.toInt() ?: 0
        val feedback = sessionFeedbackInput?.text?.toString().orEmpty()
        val sessionId = arguments?.getString(ARG_SESSION_ID).orEmpty()
        return SessionFeedback(rating, feedback, sessionId)
    }

    companion object {
        private const val ARG_SESSION_ID = "sessionId"

        fun newInstance(sessionId: String): RatingDialog {
            val args = Bundle().apply {
                putString(ARG_SESSION_ID, sessionId)
            }

            return RatingDialog().apply {
                arguments = args
            }
        }
    }
}