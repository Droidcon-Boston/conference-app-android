package com.mentalmachines.droidcon_boston.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.FaqEvent
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.views.faq.FaqAdapterItem
import com.mentalmachines.droidcon_boston.views.faq.FaqAdapterItemHeader
import eu.davidea.flexibleadapter.FlexibleAdapter
import java.util.ArrayList
import java.util.HashMap


class FAQFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance

    private lateinit var faqRecycler: RecyclerView
    private lateinit var headerAdapter: FlexibleAdapter<FaqAdapterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.faq_fragment, container, false)

        faqRecycler = view.findViewById(R.id.faq_recycler)
        fetchDataFromFirebase()
        return view
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.faqDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rows = ArrayList<FaqEvent>()
                for (faqSnapshot in dataSnapshot.children) {
                    val data = faqSnapshot.getValue(FaqEvent::class.java)
                    if (data != null) {
                        rows.add(data)
                    }
                }

                val faqList = rows.toList()
                setupHeaderAdapter(faqList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(javaClass.canonicalName, "onCancelled", databaseError.toException())
            }
        })

    }

    private fun setupHeaderAdapter(faqs: List<FaqEvent>) {
        val questionHeaders = HashMap<String, FaqAdapterItemHeader>()
        val items = ArrayList<FaqAdapterItem>(faqs.size)
        faqs.forEach { faq ->
            faq.answers.forEach { answer ->
                val header: FaqAdapterItemHeader = questionHeaders[faq.question] ?: FaqAdapterItemHeader(faq.question)
                questionHeaders[faq.question] = header

                val item = FaqAdapterItem(answer, header)
                items.add(item)
            }
        }

        faqRecycler.layoutManager = LinearLayoutManager(faqRecycler.context)
        headerAdapter = FlexibleAdapter(items)
        headerAdapter.addListener(this)
        faqRecycler.adapter = headerAdapter
        headerAdapter.expandItemsAtStartUp()
                .setDisplayHeadersAtStartUp(true)
    }

    override fun onItemClick(position: Int): Boolean {
        if (headerAdapter.getItem(position) is FaqAdapterItem) {
            val item = headerAdapter.getItem(position)
            val itemData = item!!.itemData

            val url = itemData.otherLink ?: itemData.mapLink
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            if (faqRecycler.context.packageManager.queryIntentActivities(intent, 0).size > 0) {
                startActivity(intent)
                return false
            }
        }

        return true
    }

}
