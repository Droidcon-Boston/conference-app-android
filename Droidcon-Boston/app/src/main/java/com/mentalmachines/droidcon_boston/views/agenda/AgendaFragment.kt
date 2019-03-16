package com.mentalmachines.droidcon_boston.views.agenda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mentalmachines.droidcon_boston.BuildConfig
import com.mentalmachines.droidcon_boston.R
import kotlinx.android.synthetic.main.agenda_fragment.*
import java.util.*

class AgendaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.agenda_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDayPager(savedInstanceState)
    }

    private fun setupDayPager(savedInstanceState: Bundle?) {
        viewpager.adapter = AgendaDayPagerAdapter(childFragmentManager, isMyAgenda())

        tablayout.setupWithViewPager(viewpager)

        if (savedInstanceState != null) {
            viewpager.currentItem = savedInstanceState.getInt(TAB_POSITION)
        } else {
            // set current day to second if today matches
            val today = Calendar.getInstance()
            val dayTwo = Calendar.getInstance()
            dayTwo.set(
                BuildConfig.EVENT_YEAR,
                BuildConfig.EVENT_MONTH - 1, // Calendar is 0 indexed
                BuildConfig.EVENT_DAY_TWO
            )
            if (today == dayTwo) {
                viewpager.currentItem = 1
            }
        }
    }

    fun isMyAgenda() = arguments?.getBoolean(ARG_MY_AGENDA) ?: false

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(TAB_POSITION, tablayout.selectedTabPosition)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {
                activity?.onSearchRequested()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        const val TAB_POSITION = "POSITION"

        private const val ARG_MY_AGENDA = "my_agenda"

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
