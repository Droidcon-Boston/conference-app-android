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

class RatingDialog : DialogFragment() {
    private var talkRatingBar: RatingBar? = null
    private var talkFeedbackInput: TextInputEditText? = null
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
        talkRatingBar = view.findViewById(R.id.talk_rating)
        talkFeedbackInput = view.findViewById(R.id.talk_feedback)
        cancelButton = view.findViewById(R.id.cancel)
        submitButton = view.findViewById(R.id.submit)
    }

    private fun setupClickListeners() {
        cancelButton?.setOnClickListener {
            this.dismiss()
        }

        submitButton?.setOnClickListener {
            //TODO: Handle submission
            this.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
    }
}