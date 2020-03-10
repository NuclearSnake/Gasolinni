package com.neoproduction.gasolinni

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neoproduction.gasolinni.data.RefuelRoomDB
import com.neoproduction.gasolinni.data.StationDao
import com.neoproduction.gasolinni.data.StationStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StatisticFragment : Fragment() {
    private val stations = mutableListOf<StationStats>()
    private lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_tab_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.findViewById(R.id.rvHistory)
        val layoutManager = LinearLayoutManager(view.context)
        rv.layoutManager = layoutManager
        rv.adapter = StatisticAdapter(stations, context!!.applicationContext)
        rv.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        GlobalScope.launch {
            update()
        }
    }

    private suspend fun update() {
        val refuelDB = RefuelRoomDB.getDatabase(context!!)
        val stationDao = refuelDB.stationDao()

        val stationsNew = stationDao.getStationsStatsAsync()
        stations.clear()
        stations.addAll(stationsNew)
        withContext(Dispatchers.Main) {
            rv.adapter?.notifyDataSetChanged()
        }
    }

    private suspend fun StationDao.getStationsStatsAsync() = withContext(Dispatchers.IO) {
        this@getStationsStatsAsync.getStationsStats()
    }
}