package com.mentalmachines.droidcon_boston.views.speaker


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.R.string
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.SpeakerEvent
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import kotlinx.android.synthetic.main.speaker_fragment.speaker_recycler


class SpeakerFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance
    private lateinit var speakerAdapter: FlexibleAdapter<SpeakerAdapterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.speaker_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.speakerDatabase.orderByChild("name").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rows = ArrayList<SpeakerEvent>()
                for (speakerSnapshot in dataSnapshot.children) {
                    val speaker = speakerSnapshot.getValue(SpeakerEvent::class.java)
                    if (speaker != null) {
                        rows.add(speaker)
                    }
                }

                setupSpeakerAdapter(rows)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(javaClass.canonicalName, "detailQuery:onCancelled", databaseError.toException())
            }
        })
    }

    override fun onItemClick(position: Int): Boolean {
        val item = speakerAdapter.getItem(position)
        if (item is SpeakerAdapterItem) {
            val context = activity as Context
            val simpleAlert = AlertDialog.Builder(context).create()
            simpleAlert.setTitle(item.itemData.name)
            simpleAlert.setMessage(item.itemData.bio.getHtmlFormattedSpanned())
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(string.close), { dialogInterface: DialogInterface, i: Int ->
                simpleAlert.dismiss()
            })
            simpleAlert.show()

            return false
        }

        return true // propagate.
    }

    private fun setupSpeakerAdapter(rows: ArrayList<SpeakerEvent>) {
        val items = rows.map { SpeakerAdapterItem(it) }
        speaker_recycler.layoutManager = LinearLayoutManager(speaker_recycler.context)
        speakerAdapter = FlexibleAdapter(items)
        speakerAdapter.addListener(this)
        speaker_recycler.adapter = speakerAdapter
        speaker_recycler.addItemDecoration(FlexibleItemDecoration(speaker_recycler.context).withDefaultDivider())
        speakerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(false)
    }
}
