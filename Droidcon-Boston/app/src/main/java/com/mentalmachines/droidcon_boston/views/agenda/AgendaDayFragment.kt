package com.mentalmachines.droidcon_boston.views.agenda

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.TextView
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
    private var headerAdapter: FlexibleAdapter<ScheduleAdapterItem>? = null
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var agendaRecyler: RecyclerView
    private lateinit var emptyStateView: View
    private lateinit var scrollToCurrentButton: MaterialButton

    private var hasAnyCurrentSessions = false
    private var targetCurrentSesssionPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayFilter = arguments?.getString(ARG_DAY) ?: ""
        userAgendaRepo = UserAgendaRepo.getInstance(requireContext())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val fragmentManager = activity?.supportFragmentManager
                if (fragmentManager?.backStackEntryCount!! > 0) {
                    fragmentManager.popBackStack()
                }
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
            linearSmoothScroller.targetPosition = calculateActualTargetPosition(targetCurrentSesssionPosition)
            (agendaRecyler.layoutManager as LinearLayoutManager).startSmoothScroll(linearSmoothScroller)
        }

        fetchScheduleData()

        activity?.supportFragmentManager?.addOnBackStackChangedListener(backStackChangeListener)
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

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return MILLISECONDS_PER_INCH / displayMetrics?.densityDpi!!
            }
        }
    }

    private fun updateJumpToCurrentButtonVisibility(isCurrentSessionVisible: Boolean) {
        if(isCurrentSessionVisible) {
            fadeOutJumpToCurrentButton()
        } else {
            if(hasAnyCurrentSessions) {
                fadeInJumpToCurrentButton()
            }
        }
    }

    private fun fadeOutJumpToCurrentButton() {
        if(scrollToCurrentButton.visibility == View.VISIBLE) {
            val viewPropAnimator = scrollToCurrentButton
                    .animate().alpha(0.0f).setDuration(750)
                    .setInterpolator(DecelerateInterpolator())
            viewPropAnimator.withEndAction { scrollToCurrentButton.visibility = View.GONE }
            viewPropAnimator.start()
        }
    }

    private fun fadeInJumpToCurrentButton() {
        if(scrollToCurrentButton.visibility != View.VISIBLE) {
            val viewPropAnimator = scrollToCurrentButton
                    .animate().alpha(1.0f).setDuration(750)
                    .setInterpolator(DecelerateInterpolator())
            viewPropAnimator.withStartAction { scrollToCurrentButton.visibility = View.VISIBLE }
        }
    }

    private fun addFloatingAnimation() {
        //Float up
        val propertyValuesHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 30f, -30f)
        val floatUpAnimator = ObjectAnimator.ofPropertyValuesHolder(scrollToCurrentButton, propertyValuesHolder)
        floatUpAnimator.duration = 3000
        floatUpAnimator.interpolator = LinearInterpolator()
        //Float down
        val downFloatValues = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -30f, 30f)
        val floatDownAnimator = ObjectAnimator.ofPropertyValuesHolder(scrollToCurrentButton, downFloatValues)
        floatDownAnimator.duration = 3000
        floatDownAnimator.interpolator = LinearInterpolator()

        val floatAnimation = AnimatorSet()
        floatAnimation.playSequentially(floatUpAnimator, floatDownAnimator)
        floatAnimation.start()
        floatAnimation.addListener(object : Animator.AnimatorListener {

            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                floatAnimation.start()
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

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

    private val currentSessionVisibleListener = object : RecyclerView.OnChildAttachStateChangeListener {

        override fun onChildViewAttachedToWindow(view: View) {
            if(view.tag == CURRENT_ITEM_MARKER_TAG) {
                updateJumpToCurrentButtonVisibility(true)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            if(view.tag == CURRENT_ITEM_MARKER_TAG) {
                updateJumpToCurrentButtonVisibility(false)
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
                items.sortedWith(compareBy<ScheduleAdapterItem> { it.itemData.utcStartTimeString }.thenBy { it.roomSortOrder })

        headerAdapter = FlexibleAdapter(sortedItems)
        agendaRecyler.addOnChildAttachStateChangeListener(currentSessionVisibleListener)
        headerAdapter!!.addListener(this)
        agendaRecyler.adapter = headerAdapter
        agendaRecyler.addItemDecoration(FlexibleItemDecoration(agendaRecyler.context).withDefaultDivider())
        headerAdapter!!.expandItemsAtStartUp().setDisplayHeadersAtStartUp(true)

        EmptyViewHelper(headerAdapter, emptyStateView, null, null)

        hasAnyCurrentSessions = sortedItems.any { it.itemData.isCurrentSession }
        val currentSessionItemsPosition = sortedItems.indexOfFirst { it.itemData.isCurrentSession }
        targetCurrentSesssionPosition = currentSessionItemsPosition
        updateJumpToCurrentButtonVisibility(false)
    }

    private fun calculateActualTargetPosition(currentSessionPosition: Int) : Int {

        val minIdx = 0
        val maxIdx = headerAdapter!!.itemCount - 1

        val visibleViewCount = layoutManager.findLastVisibleItemPosition() - layoutManager.findFirstVisibleItemPosition()
        val targetPosition = wrapBounds(currentSessionPosition + visibleViewCount, minIdx, maxIdx)

        return targetPosition
    }

    private fun wrapBounds(pos: Int, min: Int, max: Int) =
            if (pos >= max) { max }
            else if (pos < 0) { min }
            else { pos }

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

    companion object {
        private const val ARG_DAY = "day"
        private const val ARG_MY_AGENDA = "my_agenda"
        private const val MILLISECONDS_PER_INCH = 50f

        fun newInstance(myAgenda: Boolean, day: String): AgendaDayFragment {
            val fragment = AgendaDayFragment()
            val args = Bundle()
            args.putBoolean(ARG_MY_AGENDA, myAgenda)
            args.putString(ARG_DAY, day)
            fragment.arguments = args
            return fragment
        }
    }
}

