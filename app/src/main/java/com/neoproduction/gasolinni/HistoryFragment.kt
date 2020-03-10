package com.neoproduction.gasolinni

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neoproduction.gasolinni.data.Refuel
import com.neoproduction.gasolinni.data.RefuelDao
import com.neoproduction.gasolinni.data.RefuelRoomDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {
    val refuels = mutableListOf<Refuel>()
    lateinit var rv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_tab_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.findViewById(R.id.rvHistory)
        val layoutManager = LinearLayoutManager(view.context)
        rv.layoutManager = layoutManager
        rv.adapter = HistoryAdapter(refuels, context!!.applicationContext)
        rv.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        GlobalScope.launch {
            update()
        }
    }

    private suspend fun update() {
        val refuelDB = RefuelRoomDB.getDatabase(context!!)
        val refuelDao = refuelDB.refuelDao()

        val refuelsNew = refuelDao.getRefuelsAsync()
        refuels.clear()
        refuels.addAll(refuelsNew)
        withContext(Dispatchers.Main) {
            rv.adapter?.notifyDataSetChanged()
        }
    }

    private suspend fun RefuelDao.getRefuelsAsync() = withContext(Dispatchers.IO) {
        this@getRefuelsAsync.getRefuels()
    }
}

