package com.neoproduction.gasolinni

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.neoproduction.gasolinni.data.RefuelRoomDB
import com.neoproduction.gasolinni.data.StationDao
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.
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

class StatisticFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_tab_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        GlobalScope.launch {
            updateText(view)
        }
    }

    private suspend fun updateText(view: View) {
        val tv: TextView = view.findViewById(R.id.tvText)
        val refuelDB = RefuelRoomDB.getDatabase(context!!)
        val stationDao = refuelDB.stationDao()

        val count = stationDao.getStationsCountAsync()

        tv.text = "Statistics:\n\nSaved $count stations"
    }

    private suspend fun StationDao.getStationsCountAsync() = withContext(Dispatchers.IO) {
        this@getStationsCountAsync.getStations().size
    }
}