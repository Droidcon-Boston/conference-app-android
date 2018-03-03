package com.mentalmachines.droidcon_boston.views.agenda

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mentalmachines.droidcon_boston.R
import kotlinx.android.synthetic.main.agenda_fragment.tablayout
import kotlinx.android.synthetic.main.agenda_fragment.viewpager
import java.util.Calendar

class AgendaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.agenda_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDayPager(savedInstanceState)
    }


    private fun setupDayPager(savedInstanceState: Bundle?) {
        viewpager.adapter = AgendaDayPagerAdapter(childFragmentManager,
                arguments?.getBoolean(ARG_MY_AGENDA) ?: false)

        tablayout.setupWithViewPager(viewpager)

        if (savedInstanceState != null) {
            viewpager.currentItem = savedInstanceState.getInt(TAB_POSITION)
        } else {
            // set current day to second if today matches
            val today = Calendar.getInstance()
            val dayTwo = Calendar.getInstance()
            dayTwo.set(2018, Calendar.MARCH, 27)
            if (today == dayTwo) {
                viewpager.currentItem = 1
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(TAB_POSITION, tablayout.selectedTabPosition)
    }

    companion object {

        val TAB_POSITION = "POSITION"

        private val ARG_MY_AGENDA = "my_agenda"

        fun newInstance() = newInstance(false)
        fun newInstanceMySchedule() = newInstance(true)

        private fun newInstance(myAgenda: Boolean): AgendaFragment {
            val fragment = AgendaFragment()
            val args = Bundle()
            args.putBoolean(ARG_MY_AGENDA, myAgenda)
            fragment.arguments = args
            return fragment
        }
    }
}
