package com.mentalmachines.droidcon_boston.views.agenda

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import java.util.*

/**
 * See https://stackoverflow.com/questions/13695649/refresh-images-on-fragmentstatepageradapter-on-resuming-activity
 */
abstract class FixedFragmentStatePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val mFragments: WeakHashMap<Int, Fragment> = WeakHashMap()

    override fun getItem(position: Int): Fragment {
        val item = getFragmentItem(position)
        mFragments[Integer.valueOf(position)] = item
        return item
    }

    override fun destroyItem(container: ViewGroup, position: Int, fragmentObj: Any) {
        super.destroyItem(container, position, fragmentObj)
        val key = Integer.valueOf(position)
        if (mFragments.containsKey(key)) {
            mFragments.remove(key)
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        for (position in mFragments.keys) {
            //Make sure we only update fragments that should be seen
            if (position != null && mFragments[position] != null && position.toInt() < count) {
                updateFragmentItem(position, mFragments[position]!!)
            }
        }
    }

    override fun getItemPosition(fragmentObj: Any): Int {
        //If the object is a fragment, check to see if we have it in the hashmap
        if (fragmentObj is Fragment) {
            val position = findFragmentPositionHashMap(fragmentObj)
            //If fragment found in the hashmap check if it should be shown
            if (position >= 0) {
                //Return POSITION_NONE if it shouldn't be display
                return if (position >= count) PagerAdapter.POSITION_NONE else position
            }
        }

        return super.getItemPosition(fragmentObj)
    }

    /**
     * Find the location of a fragment in the hashmap if it being view
     * @param fragmentObj the Fragment we want to check for
     * @return the position if found else -1
     */
    private fun findFragmentPositionHashMap(fragmentObj: Fragment): Int {
        for (position in mFragments.keys) {
            if (position != null && mFragments[position] != null && mFragments[position] === fragmentObj) {
                return position
            }
        }

        return -1
    }

    abstract fun getFragmentItem(position: Int): Fragment
    abstract fun updateFragmentItem(position: Int, fragment: Fragment)
}
