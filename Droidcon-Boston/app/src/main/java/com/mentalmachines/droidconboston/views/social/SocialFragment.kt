package com.mentalmachines.droidconboston.views.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mentalmachines.droidconboston.R
import com.mentalmachines.droidconboston.R.string
import com.mentalmachines.droidconboston.modal.SocialModal
import com.mentalmachines.droidconboston.utils.DividerItemDecoration
import com.mentalmachines.droidconboston.utils.RVItemClickListener
import com.mentalmachines.droidconboston.utils.RVItemClickListener.OnItemClickListener
import com.mentalmachines.droidconboston.utils.loadUriInCustomTab
import kotlinx.android.synthetic.main.social_fragment.*
import java.util.*

class SocialFragment : Fragment() {

    private lateinit var socialList: ArrayList<SocialModal>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.social_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set Layout Manager
        social_rv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(activity)

        // Set the divider
        social_rv.addItemDecoration(
            DividerItemDecoration(
                activity!!,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
            )
        )

        socialList = prepareSocialList()
        // Set Adapter
        social_rv.adapter = RVSocialListAdapter(socialList)

        // Set On Click
        social_rv.addOnItemTouchListener(
            RVItemClickListener(requireContext(),
                object : OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        context?.loadUriInCustomTab(socialList[position].link.toString())
                    }
                })
        )
    }

    private fun prepareSocialList(): ArrayList<SocialModal> {
        val socialList = ArrayList<SocialModal>()
        socialList.add(
            SocialModal(
                R.drawable.social_facebook,
                getString(R.string.social_title_facebook),
                getString(R.string.facebook_link)
            )
        )
        socialList.add(
            SocialModal(
                R.drawable.social_instagram,
                getString(R.string.social_title_instagram),
                getString(R.string.instagram_link)
            )
        )
        socialList.add(
            SocialModal(
                R.drawable.social_linkedin,
                getString(R.string.social_title_linkedin),
                getString(R.string.linkedin_link)
            )
        )
        socialList.add(
            SocialModal(
                R.drawable.social_twitter,
                getString(R.string.social_title_twitter),
                String.format(
                    "%s%s",
                    resources.getString(R.string.twitter_link),
                    getString(string.droidconbos_twitter_handle)
                )
            )
        )
        return socialList
    }
}
