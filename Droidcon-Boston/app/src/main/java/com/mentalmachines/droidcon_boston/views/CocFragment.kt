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
import com.mentalmachines.droidcon_boston.utils.getHtmlFormattedSpanned
import kotlinx.android.synthetic.main.coc_fragment.tv_coc

class CocFragment : Fragment() {

    private val firebaseHelper = FirebaseHelper.instance

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.coc_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchDataFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        firebaseHelper.cocDatabase.removeEventListener(dataListener)
    }

    val dataListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            tv_coc.text = dataSnapshot.getValue(String::class.java)?.getHtmlFormattedSpanned()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e(javaClass.canonicalName, "onCancelled", databaseError.toException())
        }
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.cocDatabase.addValueEventListener(dataListener)
    }
}