package com.mentalmachines.droidconboston.views.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mentalmachines.droidconboston.R
import com.mentalmachines.droidconboston.data.Schedule
import com.mentalmachines.droidconboston.utils.visibleIf

/**
 * A full screen dialog that is used to provide searching functionality in the app.
 */
class SearchDialog : DialogFragment() {
    private var backButton: ImageView? = null
    private var clearButton: ImageView? = null
    private var searchInput: AutoCompleteTextView? = null

    var itemClicked: ((Schedule.ScheduleRow) -> Unit)? = null

    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
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
        listenForSchedule()
    }

    override fun onStart() {
        super.onStart()

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    private fun listenForSchedule() {
        viewModel.scheduleRows.observe(viewLifecycleOwner, Observer(this::setupSearchAdapter))
    }

    private fun setupSearchAdapter(suggestions: List<Schedule.ScheduleRow>?) {
        val adapter = ScheduleSearchAdapter(
            requireContext(),
            R.layout.list_item_schedule_search,
            suggestions.orEmpty()
        )

        searchInput?.setAdapter(adapter)
    }

    /**
     * Sets all the button click and text listeners relevant to the views within our search dialog.
     */
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

        searchInput?.setOnItemClickListener { _, _, position, _ ->
            val adapter = (searchInput?.adapter as? ScheduleSearchAdapter)
            val item = adapter?.getItem(position)
            item?.let {
                itemClicked?.invoke(it)
                dismiss()
            }
        }
    }
}
