package com.mentalmachines.droidcon_boston.views.agenda

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.mentalmachines.droidcon_boston.R
import java.util.Calendar

class AgendaFragment : Fragment() {

    @BindView(R.id.tablayout)
    lateinit var tabLayout: android.support.design.widget.TabLayout

    @BindView(R.id.viewpager)
    lateinit var viewPager: android.support.v4.view.ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.agenda_fragment, container, false)
        ButterKnife.bind(this, rootView)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDayPager(view, savedInstanceState)
    }


    private fun setupDayPager(parent: View, savedInstanceState: Bundle?) {
        viewPager = parent.findViewById<ViewPager>(R.id.viewpager)

        viewPager.adapter = AgendaDayPagerAdapter(childFragmentManager,
                arguments!!.getBoolean(ARG_MY_AGENDA))

        tabLayout = parent.findViewById<TabLayout>(R.id.tablayout)
        tabLayout.setupWithViewPager(viewPager)

        if (savedInstanceState != null) {
            viewPager.currentItem = savedInstanceState.getInt(TAB_POSITION)
        } else {
            // set current day to second if today matches
            val today = Calendar.getInstance()
            val dayTwo = Calendar.getInstance()
            dayTwo.set(2018, Calendar.MARCH, 27)
            if (today == dayTwo) {
                viewPager.currentItem = 1
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_POSITION, tabLayout.selectedTabPosition)
    }

    companion object {

        val TAB_POSITION = "POSITION"

        private val ARG_MY_AGENDA = "my_agenda"

        fun newInstance(myAgenda: Boolean): AgendaFragment {
            val fragment = AgendaFragment()
            val args = Bundle()
            args.putBoolean(ARG_MY_AGENDA, myAgenda)
            fragment.arguments = args
            return fragment
        }
    }
}
