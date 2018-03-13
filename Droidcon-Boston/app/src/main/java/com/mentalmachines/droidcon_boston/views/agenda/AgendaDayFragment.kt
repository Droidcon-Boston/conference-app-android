package com.mentalmachines.droidcon_boston.views.agenda

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEvent
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.ServiceLocator.Companion.gson
import com.mentalmachines.droidcon_boston.utils.isNullorEmpty
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import eu.davidea.flexibleadapter.helpers.EmptyViewHelper
import kotlinx.android.synthetic.main.agenda_day_fragment.*
import kotlinx.android.synthetic.main.empty_view.*
import java.util.*


/**
 * Fragment for an agenda day
 */
class AgendaDayFragment : Fragment(), FlexibleAdapter.OnItemClickListener {
    private val timeHeaders = HashMap<String, ScheduleAdapterItemHeader>()

    private var dayFilter: String = ""
    private val firebaseHelper = FirebaseHelper.instance
    private var onlyMyAgenda: Boolean = false

    private lateinit var userAgendaRepo: UserAgendaRepo
    private lateinit var headerAdapter: FlexibleAdapter<ScheduleAdapterItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dayFilter = arguments?.getString(ARG_DAY) ?: ""
        userAgendaRepo = UserAgendaRepo.getInstance(context!!)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                val fragmentManager = activity?.fragmentManager
                if (fragmentManager?.backStackEntryCount!! > 0) {
                    fragmentManager.popBackStack()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.agenda_day_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        agenda_recycler.layoutManager = LinearLayoutManager(activity?.applicationContext)

        onlyMyAgenda = arguments?.getBoolean(ARG_MY_AGENDA) ?: false

        fetchScheduleData(dayFilter, onlyMyAgenda)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        firebaseHelper.eventDatabase.removeEventListener(dataListener)
    }

    fun updateList() {
        agenda_recycler.adapter.notifyDataSetChanged()
    }

    val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<ScheduleRow>()
            for (roomSnapshot in dataSnapshot.children) {
                val key = roomSnapshot.key
                val data = roomSnapshot.getValue(ScheduleEvent::class.java)
                Log.d(TAG, "Event: $data")
                if (data != null) {
                    val scheduleRow = data.toScheduleRow(key)
                    if (scheduleRow.date == dayFilter && (!onlyMyAgenda
                                    || onlyMyAgenda && userAgendaRepo.isSessionBookmarked(scheduleRow.id))) {
                        rows.add(scheduleRow)
                    }
                }
            }

            setupHeaderAdapter(rows)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.w(TAG, "scheduleQuery:onCancelled", databaseError.toException())
        }
    }

    private fun fetchScheduleData(dayFilter: String?, onlyMyAgenda: Boolean) {
        firebaseHelper.eventDatabase.addListenerForSingleValueEvent(dataListener)
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

        val sortedItems = items.sortedWith(
                compareBy<ScheduleAdapterItem> { it.itemData.utcStartTimeString }
                        .thenBy { it.roomSortOrder })

        headerAdapter = FlexibleAdapter(sortedItems)
        headerAdapter.addListener(this)
        agenda_recycler.adapter = headerAdapter
        agenda_recycler.addItemDecoration(FlexibleItemDecoration(agenda_recycler.context).withDefaultDivider())
        headerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(true)

        EmptyViewHelper(headerAdapter, empty_view, null,null)
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        if (headerAdapter.getItem(position) is ScheduleAdapterItem) {
            val item = headerAdapter.getItem(position)
            val itemData = item?.itemData
            if (itemData?.primarySpeakerName.isNullorEmpty()) {
                val url = itemData?.photoUrlMap?.get(itemData.primarySpeakerName)

                if (!url.isNullorEmpty()) {
                    // event where info URL is in the photoUrls string
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    val packageManager = activity?.packageManager
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i)
                    }
                    return false
                }
            }
            val arguments = Bundle()
            arguments.putString(Schedule.SCHEDULE_ITEM_ROW, gson.toJson(itemData, ScheduleRow::class.java))

            val agendaDetailFragment = AgendaDetailFragment()
            agendaDetailFragment.arguments = arguments

            val fragmentManager = activity?.fragmentManager
            fragmentManager?.beginTransaction()
                    ?.add(R.id.fragment_container, agendaDetailFragment)
                    ?.addToBackStack(null)
                    ?.commit()
        }

        return true
    }

    companion object {

        private val TAG = AgendaDayFragment::class.java.name
        private const val ARG_DAY = "day"
        private const val ARG_MY_AGENDA = "my_agenda"

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

