package com.mentalmachines.droidcon_boston.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R.layout
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.FaqEvent
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import com.mentalmachines.droidcon_boston.views.faq.FaqAdapterItem
import com.mentalmachines.droidcon_boston.views.faq.FaqAdapterItemHeader
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.android.synthetic.main.faq_fragment.*
import timber.log.Timber
import java.util.*


class FAQFragment : Fragment(), FlexibleAdapter.OnItemClickListener {

    private val firebaseHelper = FirebaseHelper.instance

    private lateinit var headerAdapter: FlexibleAdapter<FaqAdapterItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(layout.faq_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchDataFromFirebase()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        firebaseHelper.faqDatabase.removeEventListener(dataListener)
    }

    private val dataListener: ValueEventListener = object : ValueEventListener {
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
            Timber.e(databaseError.toException())
        }
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.faqDatabase.addValueEventListener(dataListener)
    }

    private fun setupHeaderAdapter(faqs: List<FaqEvent>) {
        val questionHeaders = HashMap<String, FaqAdapterItemHeader>()
        val items = ArrayList<FaqAdapterItem>(faqs.size)
        faqs.forEach { faq ->
            faq.answers.forEach { answer ->
                val header: FaqAdapterItemHeader =
                    questionHeaders[faq.question] ?: FaqAdapterItemHeader(faq.question)
                questionHeaders[faq.question] = header

                val item = FaqAdapterItem(answer, header)
                items.add(item)
            }
        }

        faq_recycler.layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(faq_recycler.context)
        headerAdapter = FlexibleAdapter(items)
        headerAdapter.addListener(this)
        faq_recycler.adapter = headerAdapter
        headerAdapter.expandItemsAtStartUp().setDisplayHeadersAtStartUp(true)
    }

    override fun onItemClick(view: View, position: Int): Boolean {
        if (headerAdapter.getItem(position) is FaqAdapterItem) {
            val item = headerAdapter.getItem(position)
            val itemData = item!!.itemData

            val url =
                if (!TextUtils.isEmpty(itemData.otherLink)) itemData.otherLink else itemData.mapLink
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            if (faq_recycler.context.packageManager.queryIntentActivities(intent, 0).size > 0) {
                startActivity(intent)
                return false
            }
        }

        return true
    }

}
