package com.mentalmachines.droidcon_boston.views.search

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.mentalmachines.droidcon_boston.R
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import com.mentalmachines.droidcon_boston.utils.visibleIf
import timber.log.Timber

class SearchDialog : DialogFragment() {
    private var backButton: ImageView? = null
    private var clearButton: ImageView? = null
    private var searchInput: AutoCompleteTextView? = null

    var queryListener: ((String) -> Unit)? = null

    private val currentQuery: String
        get() = searchInput?.text?.toString().orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton = view.findViewById(R.id.action_back)
        clearButton = view.findViewById(R.id.action_clear)
        searchInput = view.findViewById(R.id.search_input)

        setupViewListeners()
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    private fun setupViewListeners() {
        backButton?.setOnClickListener {
            dismiss()
        }

        clearButton?.setOnClickListener {
            searchInput?.setText("")
        }

        searchInput?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Not Needed
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not Needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton?.visibleIf(s?.isNotEmpty())
            }
        })

        searchInput?.setOnEditorActionListener { _, _, _ ->
            handleQuery()
            true
        }
    }

    private fun handleQuery() {
        Timber.d("Searched for: $currentQuery")
        queryListener?.invoke(currentQuery)
        dismiss()
    }

    /**
     * In general, we can just dismiss the dialog without performing a search. However, if we
     * dismiss the dialog and there is no search, we should send that out so we can clear any
     * current searches.
     */
    override fun onDismiss(dialog: DialogInterface?) {
        if (currentQuery.isEmpty()) {
            handleQuery()
        } else {
            super.dismiss()
        }
    }
}