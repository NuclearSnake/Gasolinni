package com.neoproduction.gasolinni

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    val fragmentNames = listOf(
        activity.getString(R.string.main_tab_name_history),
        activity.getString(R.string.main_tab_name_statistics)
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> HistoryFragment()
        else -> StatisticsFragment()
    }
}