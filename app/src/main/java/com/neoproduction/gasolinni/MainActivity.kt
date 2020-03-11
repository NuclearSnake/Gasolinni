package com.neoproduction.gasolinni

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()

    private lateinit var mainFragmentStateAdapter: MainFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragmentStateAdapter = MainFragmentStateAdapter(this)
        pager.adapter = mainFragmentStateAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = mainFragmentStateAdapter.fragmentNames[position]
        }.attach()

        fab.setOnClickListener { vm.onFabClick(this) }
    }
}

class MainFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    val fragmentNames = listOf(
        activity.getString(R.string.main_tab_name_history),
        activity.getString(R.string.main_tab_name_statistics)
    )

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> HistoryFragment()
        else -> StatisticFragment()
    }
}