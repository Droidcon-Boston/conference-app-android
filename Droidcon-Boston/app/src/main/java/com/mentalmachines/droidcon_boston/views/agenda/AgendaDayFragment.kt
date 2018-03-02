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
import android.widget.Toast
import com.dinuscxj.refresh.RecyclerRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.ScheduleEvent
import com.mentalmachines.droidcon_boston.data.Schedule
import com.mentalmachines.droidcon_boston.data.Schedule.ScheduleRow
import com.mentalmachines.droidcon_boston.data.UserAgendaRepo
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.StringUtils
import com.mentalmachines.droidcon_boston.views.detail.AgendaDetailFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import kotlinx.android.synthetic.main.agenda_day_fragment.agenda_recycler
import kotlinx.android.synthetic.main.agenda_day_fragment.refresh_layout
import java.util.ArrayList
import java.util.HashMap

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
        when (item!!.itemId) {
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

        refresh_layout.setRefreshStyle(RecyclerRefreshLayout.RefreshStyle.NORMAL)

        refresh_layout.setOnRefreshListener {
            headerAdapter.clear()
            headerAdapter.notifyDataSetChanged()
            refresh_layout.setRefreshing(true)
            fetchScheduleData(dayFilter, onlyMyAgenda)
            Toast.makeText(activity, "Refreshed Agenda", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateList() {
        agenda_recycler.adapter.notifyDataSetChanged()
    }

    private fun fetchScheduleData(dayFilter: String?, onlyMyAgenda: Boolean) {
        firebaseHelper.eventDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rows = ArrayList<ScheduleRow>()
                for (roomSnapshot in dataSnapshot.children) {
                    val data = roomSnapshot.getValue(ScheduleEvent::class.java)
                    Log.d(TAG, "Event: " + data)
                    if (data != null) {
                        val scheduleRow = data.toScheduleRow()
                        if (scheduleRow.date == dayFilter && (!onlyMyAgenda || onlyMyAgenda && userAgendaRepo.isSessionBookmarked(scheduleRow.id))) {
                            rows.add(scheduleRow)
                        }
                    }
                }

                refresh_layout.setRefreshing(false)
                setupHeaderAdapter(rows)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "scheduleQuery:onCancelled", databaseError.toException())
            }
        })

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
        headerAdapter.expandItemsAtStartUp()
                .setDisplayHeadersAtStartUp(true)
    }

    override fun onItemClick(position: Int): Boolean {
        if (headerAdapter.getItem(position) is ScheduleAdapterItem) {
            val item = headerAdapter.getItem(position)
            val itemData = item?.itemData
            if (StringUtils.isNullorEmpty(itemData?.primarySpeakerName)) {
                val url = itemData?.photoUrlMap?.get(itemData.primarySpeakerName)
                
                // event where info URL is in the photoUrls string
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                val packageManager = activity?.packageManager
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                }
                return false
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
        private val gson = Gson()

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
