package com.mentalmachines.droidcon_boston.views.volunteer


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.VolunteerEvent
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.loadUriInCustomTab
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import kotlinx.android.synthetic.main.volunteer_fragment.*


class VolunteerFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance
    private lateinit var volunteerAdapter: FlexibleAdapter<VolunteerAdapterItem>

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.volunteer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        firebaseHelper.volunteerDatabase.removeEventListener(dataListener)
    }

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<VolunteerEvent>()
            for (volunteerSnapshot in dataSnapshot.children) {
                val volunteer = volunteerSnapshot.getValue(VolunteerEvent::class.java)
                if (volunteer != null) {
                    rows.add(volunteer)
                }
            }

            setupVolunteerAdapter(rows)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(javaClass.canonicalName, "detailQuery:onCancelled", databaseError.toException())
        }
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.volunteerDatabase.orderByChild("firstName")
                .addValueEventListener(dataListener)
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        val item = volunteerAdapter.getItem(position)
        if (item is VolunteerAdapterItem && !item.itemData.twitter.isEmpty()) {
            val context = activity as Context
            context.loadUriInCustomTab(String.format("%s%s",
                    resources.getString(R.string.twitter_link),
                    item.itemData.twitter))
            return false
        }

        return true // propagate.
    }


    private fun setupVolunteerAdapter(rows: ArrayList<VolunteerEvent>) {
        val items = rows.map { VolunteerAdapterItem(it) }
        volunteer_recycler.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(volunteer_recycler.context)
        volunteerAdapter = FlexibleAdapter(items)
        volunteerAdapter.addListener(this)
        volunteer_recycler.adapter = volunteerAdapter
        volunteer_recycler.addItemDecoration(FlexibleItemDecoration(volunteer_recycler.context).withDefaultDivider())
        volunteerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(false)
    }
}
