package com.mentalmachines.droidcon_boston.views.agenda

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEvent
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.isNullorEmpty
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment
import com.mentalmachines.droidcon_boston.views.search.SearchDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import timber.log.Timber

/**
 * Fragment for an agenda day
 */
class AgendaDayFragment : Fragment(), FlexibleAdapter.OnItemClickListener {
    private val timeHeaders = HashMap<String, ScheduleAdapterItemHeader>()

    private var dayFilter: String = ""
    private val firebaseHelper = FirebaseHelper.instance
    private var onlyMyAgenda: Boolean = false

    private lateinit var userAgendaRepo: UserAgendaRepo
    private var headerAdapter: FlexibleAdapter<*>? = null
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var agendaRecyler: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var scrollToCurrentButton: MaterialButton

    /**
     * Total number of sessions that begin after now and end before now.
     * where 'now' was determined when these items were loaded from Firebase.
     */
    private var totalCurrentSessionCount = 0

    /**
     * Target scroll to position when Jump to current is clicked.
     */
    private var targetCurrentSesssionPosition = 0

    /**
     * Number of sessions that begin after now and end before now, which are currently
     * attached to the RecyclerView (in the users view).
     */
    private var visibleCurrentSessionCount = 0
        set(value) {
            field = wrapBounds(value, 0, totalCurrentSessionCount)
            field = value
            updateJumpToCurrentButtonVisibility(value > 0)
        }

    private fun wrapBounds(value: Int, min: Int, max: Int) =
        if (value >= max) {
            Timber.w("Value out of bounds value=$value, min=$min, max=$max")
            max
        } else if (value < 0) {
            Timber.w("Value out of bounds value=$value, min=$min, max=$max")
            min
        } else {
            value
        }

    private val searchDialog = SearchDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayFilter = arguments?.getString(ARG_DAY) ?: ""
        userAgendaRepo = UserAgendaRepo.getInstance(requireContext())
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val fragmentManager = activity?.supportFragmentManager
                if (fragmentManager?.backStackEntryCount!! > 0) {
                    fragmentManager.popBackStack()
                }
            }
            R.id.search -> {
                searchDialog.show(fragmentManager, SEARCH_DIALOG_TAG)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agenda_day_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // NOTE: Kotlin Extensions' agenda_vew is null in setupHeaderAdapter sporadically, so do this old school
        agendaRecyler = view.findViewById(R.id.agenda_recycler)
        emptyStateView = view.findViewById(R.id.empty_view)
        scrollToCurrentButton = view.findViewById(R.id.scroll_to_current_session)

        layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        agendaRecyler.layoutManager = layoutManager
        onlyMyAgenda = arguments?.getBoolean(ARG_MY_AGENDA) ?: false

        val linearSmoothScroller = setupSmoothScroller()
        addFloatingAnimation()
        scrollToCurrentButton.setOnClickListener {
            linearSmoothScroller.targetPosition = targetCurrentSesssionPosition
            (agendaRecyler.layoutManager as LinearLayoutManager).startSmoothScroll(
                linearSmoothScroller
            )
        }

        fetchScheduleData()

        activity?.supportFragmentManager?.addOnBackStackChangedListener(backStackChangeListener)
        listenForSearchQueries()
    }

    private val backStackChangeListener: () -> Unit = {
        if (onlyMyAgenda) {
            fetchScheduleData()
        } else {
            headerAdapter?.notifyDataSetChanged()
        }
    }

    private fun setupSmoothScroller(): RecyclerView.SmoothScroller {
        return object : LinearSmoothScroller(context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi
            }
        }
    }

    private fun updateJumpToCurrentButtonVisibility(isCurrentSessionVisible: Boolean) {
        if (isCurrentSessionVisible) {
            fadeOutJumpToCurrentButton()
        } else {
            if (totalCurrentSessionCount > 0) {
                fadeInJumpToCurrentButton()
            }
        }
    }

    private fun fadeOutJumpToCurrentButton() {
        val viewPropAnimator = scrollToCurrentButton
            .animate()
            .alpha(JumpToCurrent.ButtonVisibility.minAlpha)
            .setDuration(JumpToCurrent.ButtonVisibility.duration)
            .setInterpolator(DecelerateInterpolator())
        viewPropAnimator.withEndAction { scrollToCurrentButton.visibility = View.GONE }
        viewPropAnimator.start()
    }

    private fun fadeInJumpToCurrentButton() {
        val viewPropAnimator = scrollToCurrentButton
            .animate().alpha(JumpToCurrent.ButtonVisibility.maxAlpha)
            .setDuration(JumpToCurrent.ButtonVisibility.duration)
            .setInterpolator(DecelerateInterpolator())
        viewPropAnimator.withStartAction { scrollToCurrentButton.visibility = View.VISIBLE }
    }

    private fun addFloatingAnimation() {

        // Float up
        val propertyValuesHolder = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_Y,
            JumpToCurrent.ButtonTranslation.translationY,
            -JumpToCurrent.ButtonTranslation.translationY
        )

        val floatUpAnimator = ObjectAnimator.ofPropertyValuesHolder(
            scrollToCurrentButton, propertyValuesHolder
        )
        floatUpAnimator.duration = JumpToCurrent.ButtonTranslation.duration
        floatUpAnimator.interpolator = LinearInterpolator()

        // Float down
        val downFloatValues = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_Y,
            -JumpToCurrent.ButtonTranslation.translationY,
            JumpToCurrent.ButtonTranslation.translationY
        )
        val floatDownAnimator = ObjectAnimator.ofPropertyValuesHolder(
            scrollToCurrentButton, downFloatValues
        )
        floatDownAnimator.duration = JumpToCurrent.ButtonTranslation.duration
        floatDownAnimator.interpolator = LinearInterpolator()

        val floatAnimation = AnimatorSet()
        floatAnimation.playSequentially(floatUpAnimator, floatDownAnimator)
        floatAnimation.start()
        floatAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                floatAnimation.start()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        agendaRecyler.removeOnChildAttachStateChangeListener(currentSessionVisibleListener)
        firebaseHelper.eventDatabase.removeEventListener(dataListener)
        activity?.supportFragmentManager?.removeOnBackStackChangedListener(backStackChangeListener)
    }

    fun updateList() {
        agendaRecyler.adapter?.notifyDataSetChanged()
    }

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<ScheduleRow>()
            for (roomSnapshot in dataSnapshot.children) {
                val key = roomSnapshot.key ?: ""
                val data = roomSnapshot.getValue(ScheduleEvent::class.java)
                Timber.d("Event: $data")
                if (data != null) {
                    val scheduleRow = data.toScheduleRow(key)
                    val matchesDay = scheduleRow.date == dayFilter
                    val isPublicView = !onlyMyAgenda
                    val isPrivateAndBookmarked = onlyMyAgenda && userAgendaRepo
                        .isSessionBookmarked(scheduleRow.id)

                    if (matchesDay && (isPublicView || isPrivateAndBookmarked)) {
                        rows.add(scheduleRow)
                    }
                }
            }
            setupHeaderAdapter(rows)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Timber.e(databaseError.toException())
        }
    }

    private fun fetchScheduleData() {
        firebaseHelper.eventDatabase.addValueEventListener(dataListener)
    }

    private val currentSessionVisibleListener =
        object : RecyclerView.OnChildAttachStateChangeListener {

            override fun onChildViewAttachedToWindow(view: View) {
                if (view.tag == CURRENT_ITEM_MARKER_TAG) {
                    visibleCurrentSessionCount++
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                if (view.tag == CURRENT_ITEM_MARKER_TAG) {
                    visibleCurrentSessionCount--
                }
            }
        }

    private fun setupHeaderAdapter(rows: List<ScheduleRow>) {
        val items = ArrayList<ScheduleAdapterItem>(rows.size)
        for (row in rows) {
            val timeDisplay = if (row.startTime.isEmpty()) "Unscheduled" else row.startTime
            var header: ScheduleAdapterItemHeader? = timeHeaders[timeDisplay]
            if (header == null) {
                header = ScheduleAdapterItemHeader(timeDisplay)
                timeHeaders[timeDisplay] = header
            }

            val item = ScheduleAdapterItem(row, header)
            items.add(item)
        }

        val sortedItems =
            items.sortedWith(compareBy<ScheduleAdapterItem> { it.itemData.utcStartTimeString }
                .thenBy { it.roomSortOrder })

        headerAdapter = FlexibleAdapter(sortedItems)
        agendaRecyler.addOnChildAttachStateChangeListener(currentSessionVisibleListener)
        headerAdapter!!.addListener(this)
        agendaRecyler.adapter = headerAdapter
        agendaRecyler
            .addItemDecoration(FlexibleItemDecoration(agendaRecyler.context)
            .withDefaultDivider())
        headerAdapter!!.expandItemsAtStartUp().setDisplayHeadersAtStartUp(true)

        EmptyViewHelper(headerAdapter, emptyStateView, null, null)

        initializeJumpButtonVariables(sortedItems)
    }

    private fun initializeJumpButtonVariables(sortedItems: List<ScheduleAdapterItem>) {

        // Total number of sessions that begin before now and end after now.
        // where 'now' was determined when these items were loaded from Firebase.
        totalCurrentSessionCount = sortedItems.count { it.itemData.isCurrentSession }

        if (totalCurrentSessionCount > 0) {
            // Scroll target when jump to now selected.
            val currentItems = headerAdapter!!.currentItems
            val indexOfFirstCurrentSession = currentItems.indexOfFirst {
                (it is ScheduleAdapterItem) && it.itemData.isCurrentSession
            }
            targetCurrentSesssionPosition =
                    minOf(indexOfFirstCurrentSession, currentItems.lastIndex)
        }

        // Initialize the number of visible current sessions to zero.
        visibleCurrentSessionCount = 0
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        val adapterItem = try {
            headerAdapter?.getItem(position)
        } catch (e: Exception) {
            null
        }
        if (adapterItem is ScheduleAdapterItem) {
            val itemData = adapterItem.itemData
            if (itemData.primarySpeakerName.isNullorEmpty()) {
                val url = itemData.photoUrlMap[itemData.primarySpeakerName]

                if (!url.isNullorEmpty()) {
                    // event where info URL is in the photoUrls string
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    val packageManager = activity?.packageManager
                    if (packageManager != null && i.resolveActivity(packageManager) != null) {
                        startActivity(i)
                    }
                    return false
                }
            }

            activity?.let {
                AgendaDetailFragment.addDetailFragmentToStack(it.supportFragmentManager, itemData)
            }
        }

        return true
    }

    private fun listenForSearchQueries() {
        searchDialog.queryListener = { query ->
            headerAdapter?.setFilter(query)
            headerAdapter?.filterItems()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
    }

    companion object {
        private const val ARG_DAY = "day"
        private const val ARG_MY_AGENDA = "my_agenda"
        private const val MILLISECONDS_PER_INCH = 50f
        private const val SEARCH_DIALOG_TAG = "agenda_search_tag"

        fun newInstance(myAgenda: Boolean, day: String): AgendaDayFragment {
            val fragment = AgendaDayFragment()
            val args = Bundle()
            args.putBoolean(ARG_MY_AGENDA, myAgenda)
            args.putString(ARG_DAY, day)
            fragment.arguments = args
            return fragment
        }
    }

    object JumpToCurrent {

        object ButtonVisibility {
            const val minAlpha = 0.0f
            const val maxAlpha = 1.0f
            const val duration = 750L
        }

        object ButtonTranslation {
            const val duration = 3000L
            const val translationY = 30.0f
        }
    }
}
