package com.mentalmachines.droidcon_boston.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import kotlinx.android.synthetic.main.about_fragment.tv_about_description

class AboutFragment : Fragment() {

    private val firebaseHelper = FirebaseHelper.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.about_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.aboutDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tv_about_description.text = dataSnapshot.getValue(String::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(javaClass.canonicalName, "onCancelled", databaseError.toException())
            }
        })

    }
}
