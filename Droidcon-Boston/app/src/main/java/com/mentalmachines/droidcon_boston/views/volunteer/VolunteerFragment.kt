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
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.VolunteerEvent
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration
import kotlinx.android.synthetic.main.volunteer_fragment.volunteer_recycler


class VolunteerFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance
    private lateinit var volunteerAdapter: FlexibleAdapter<VolunteerAdapterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.volunteer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.volunteerDatabase.orderByChild("name").addValueEventListener(object : ValueEventListener {
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
        })
    }

    override fun onItemClick(position: Int): Boolean {
        val item = volunteerAdapter.getItem(position)
        if (item is VolunteerAdapterItem) {
            val context = activity as Context
            val simpleAlert = AlertDialog.Builder(context).create()
            simpleAlert.setTitle("${item.itemData.firstName} ${item.itemData.lastName}")

            // Setup body text
            var bodyText = item.itemData.position
            if (!item.itemData.twitter.isNullOrEmpty()) {
                bodyText += "\nTwitter: @${item.itemData.twitter}"
            }

            if (!item.itemData.email.isNullOrEmpty()) {
                bodyText += "\nEmail: ${item.itemData.email}"
            }

            simpleAlert.setMessage(bodyText)
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, getString(string.close), { dialogInterface: DialogInterface, _: Int ->
                simpleAlert.dismiss()
            })
            simpleAlert.show()

            return false
        }

        return true // propagate.
    }

    private fun setupVolunteerAdapter(rows: ArrayList<VolunteerEvent>) {
        val items = rows.map { VolunteerAdapterItem(it) }
        volunteer_recycler.layoutManager = LinearLayoutManager(volunteer_recycler.context)
        volunteerAdapter = FlexibleAdapter(items)
        volunteerAdapter.addListener(this)
        volunteer_recycler.adapter = volunteerAdapter
        volunteer_recycler.addItemDecoration(FlexibleItemDecoration(volunteer_recycler.context).withDefaultDivider())
        volunteerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(false)
    }
}
