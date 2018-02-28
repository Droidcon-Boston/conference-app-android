package com.mentalmachines.droidcon_boston.views

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mentalmachines.droidcon_boston.R
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.FaqEvent
import com.mentalmachines.droidcon_boston.data.FirebaseDatabase.FaqEvent.Answer
import com.mentalmachines.droidcon_boston.firebase.FirebaseHelper
import java.util.ArrayList
import java.util.HashMap

class FAQFragment : Fragment() {

    private val firebaseHelper = FirebaseHelper.instance

    private lateinit var expandableListView: ExpandableListView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.faq_fragment, container, false)
        expandableListView = view.findViewById<ExpandableListView>(R.id.faqlist)
        fetchDataFromFirebase()
        return view
    }

    private fun fetchDataFromFirebase() {
        firebaseHelper.faqDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val rows = ArrayList<FaqEvent>()
                for (faqSnapshot in dataSnapshot.children) {
                    val data = faqSnapshot.getValue(FaqEvent::class.java)
                    if (data != null) {
                        rows.add(data)
                    }
                }

                expandableListView.setAdapter(FaqExpandable(rows.toList()))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(javaClass.canonicalName, "onCancelled", databaseError.toException())
            }
        })

    }

    inner class FaqExpandable(faqData: List<FaqEvent>) : BaseExpandableListAdapter() {

        val questionsGroup: List<String>
        val childrens: HashMap<Int, List<Answer>>

        init {
            questionsGroup = faqData.map { it.question }
            childrens = HashMap()
            faqData.forEachIndexed {i, faq ->
                childrens.put(i, faq.answers)
            }
        }

        override fun getGroupCount(): Int {
            return questionsGroup.size
        }

        override fun getChildrenCount(position: Int): Int {
            return if (childrens[position] == null) 0 else childrens[position]!!.size
        }

        override fun getGroup(position: Int): List<Answer>? {
            return childrens.get(position)
        }

        override fun getChild(groupId: Int, dex: Int): Answer? {
            return childrens[groupId]?.get(dex)
        }

        override fun getGroupId(position: Int): Long {
            return position.toLong()
        }

        override fun getChildId(groupPosition: Int, childPosition: Int): Long {
            return (groupPosition * 100 + childPosition).toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.faq_header, null)
            }
            (convertView!!.findViewById<View>(R.id.question_text) as TextView).text = questionsGroup[groupPosition]
            return convertView
        }

        override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?,
                                  parent: ViewGroup): View {
            var convertView = convertView
            val item = childrens[groupPosition]!![childPosition]
            val ctx = context
            if (convertView == null) {
                convertView = LayoutInflater.from(ctx).inflate(R.layout.faq_item, null)
                convertView!!.tag = convertView.findViewById(R.id.q_top)
                convertView.setTag(R.id.q_photo_row, convertView.findViewById(R.id.q_photo_row))
                convertView.setTag(R.id.q_button_row, convertView.findViewById(R.id.q_button_row))
            }
            if (childPosition == 0) {
                (convertView.tag as View).visibility = View.GONE
            } else {
                (convertView.tag as View).visibility = View.VISIBLE
            }

            (convertView.findViewById<View>(R.id.q_text) as TextView).text = item.answer
            if (TextUtils.isEmpty(item.photoLink) && TextUtils.isEmpty(item.mapLink) &&
                    TextUtils.isEmpty(item.otherLink)) {
                return convertView
            }
            val more: ImageButton
            val map: ImageButton
            var photoLayout: LinearLayout? = null
            val buttonRow = convertView.getTag(R.id.q_button_row) as LinearLayout
            if (!TextUtils.isEmpty(item.photoLink)) {
                buttonRow.visibility = View.GONE
                photoLayout = convertView.getTag(R.id.q_photo_row) as LinearLayout
                photoLayout.visibility = View.VISIBLE
                Glide.with(ctx)
                        .load(item.photoLink)
                        .override(600, 600)
                        .centerCrop()
                        .into(photoLayout.findViewById<View>(R.id.q_photo) as ImageView)
                photoLayout = photoLayout.findViewById(R.id.q_button_col)
                more = photoLayout!!.findViewById(R.id.q_more_p)
                map = photoLayout.findViewById(R.id.q_map_p)
            } else {
                (convertView.getTag(R.id.q_photo_row) as View).visibility = View.GONE
                Log.i(TAG, "no photo " + childPosition)
                more = buttonRow.findViewById(R.id.q_more)
                map = buttonRow.findViewById(R.id.q_map)
            }

            //item may not have either bizLink or mapLink or both
            if (TextUtils.isEmpty(item.otherLink) && TextUtils.isEmpty(item.mapLink)) {
                Log.d(TAG, "hide buttons")
                if (photoLayout == null) {
                    buttonRow.visibility = View.GONE
                } else {
                    photoLayout.visibility = View.GONE
                }
            } else {
                //has either geo or biz link to set data on a view intent
                Log.d(TAG, "show buttons")
                if (photoLayout != null) {
                    //this is the row of buttons now
                    photoLayout.visibility = View.VISIBLE
                } else {
                    buttonRow.visibility = View.VISIBLE
                }
                if (TextUtils.isEmpty(item.mapLink)) {
                    map.visibility = View.GONE
                } else {
                    map.visibility = View.VISIBLE
                    map.tag = item.mapLink
                }
                if (TextUtils.isEmpty(item.otherLink)) {
                    more.visibility = View.GONE
                } else {
                    more.visibility = View.VISIBLE
                    more.tag = item.otherLink
                }
            }
            return convertView
        }

        override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
            return true
        }
    }

    companion object {
        val TAG = "FAQFragment"
    }

}
