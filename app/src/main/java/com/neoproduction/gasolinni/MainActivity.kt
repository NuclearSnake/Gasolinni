package com.neoproduction.gasolinni

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainFragmentStateAdapter: MainFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragmentStateAdapter = MainFragmentStateAdapter(this)
        pager.adapter = mainFragmentStateAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = when (position) {
                0 -> "History"
                else -> "Statistics"
            }
        }.attach()

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddGasStationActivity::class.java)
            startActivity(intent)
        }
    }
}

class MainFragmentStateAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> HistoryFragment()
        else -> StatisticFragment()
    }
}