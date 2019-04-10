package com.mentalmachines.droidconboston.views.speaker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidconboston.R
import com.mentalmachines.droidconboston.data.FirebaseDatabase.EventSpeaker
import com.mentalmachines.droidconboston.firebase.FirebaseHelper
import com.mentalmachines.droidconboston.utils.ServiceLocator.Companion.gson
import com.mentalmachines.droidconboston.views.detail.SpeakerDetailFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import kotlinx.android.synthetic.main.speaker_fragment.*
import timber.log.Timber


class SpeakerFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance
    private lateinit var speakerAdapter: FlexibleAdapter<SpeakerAdapterItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.speaker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        firebaseHelper.speakerDatabase.removeEventListener(dataListener)
    }

    private val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val rows = ArrayList<EventSpeaker>()
            for (speakerSnapshot in dataSnapshot.children) {
                val speaker = speakerSnapshot.getValue(EventSpeaker::class.java)
                if (speaker != null) {
                    rows.add(speaker)
                }
            }

            setupSpeakerAdapter(rows)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Timber.e(databaseError.toException())
        }
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.speakerDatabase.orderByChild("name").addValueEventListener(dataListener)
    }

    override fun onItemClick(view: View, position: Int): Boolean {

        if (speakerAdapter.getItem(position) is SpeakerAdapterItem) {
            val item = speakerAdapter.getItem(position)
            val itemData = item?.itemData

            val arguments = Bundle()

            arguments.putString(
                EventSpeaker.SPEAKER_ITEM_ROW,
                gson.toJson(itemData, EventSpeaker::class.java)
            )

            val speakerDetailFragment = SpeakerDetailFragment()
            speakerDetailFragment.arguments = arguments

            val fragmentManager = activity?.supportFragmentManager
            fragmentManager?.beginTransaction()?.add(R.id.fragment_container, speakerDetailFragment)
                ?.addToBackStack(null)?.commit()
        }

        return true
    }

    private fun setupSpeakerAdapter(rows: ArrayList<EventSpeaker>) {
        val items = rows.map { SpeakerAdapterItem(it) }
        speaker_recycler.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(speaker_recycler.context)
        speakerAdapter = FlexibleAdapter(items)
        speakerAdapter.addListener(this)
        speaker_recycler.adapter = speakerAdapter
        speaker_recycler.addItemDecoration(FlexibleItemDecoration(speaker_recycler.context).withDefaultDivider())
        speakerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(false)
    }
}
