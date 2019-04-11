package com.mentalmachines.droidcon_boston.views.agenda

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mentalmachines.droidcon_boston.BuildConfig
import com.mentalmachines.droidcon_boston.data.Schedule

class AgendaDayPagerAdapter internal constructor(
    fm: FragmentManager,
    private val myAgenda: Boolean
) :
    FixedFragmentStatePagerAdapter(fm) {

    private val pageCount = 2
    private val tabTitles = arrayOf("Day 1", "Day 2")

    override fun getCount(): Int {
        return pageCount
    }

    override fun getItem(position: Int): Fragment {
        val dayString = if (position == 0) {
            BuildConfig.EVENT_DAY_ONE_STRING
        } else {
            BuildConfig.EVENT_DAY_TWO_STRING
        }

        return AgendaDayFragment.newInstance(
            myAgenda,
            dayString
        )
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun updateFragmentItem(position: Int, fragment: Fragment) {
        if (fragment is AgendaDayFragment) {
            fragment.updateList()
        }
    }

    override fun getFragmentItem(position: Int): Fragment {
        return getItem(position)
    }
}
