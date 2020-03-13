package com.neoproduction.gasolinni.ui.main.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.neoproduction.gasolinni.R
import com.neoproduction.gasolinni.ui.main.MainViewModel

class StatisticsFragment : Fragment() {
    private val vm: MainViewModel by activityViewModels()

    private lateinit var rv: RecyclerView
    private lateinit var adapter: StatisticAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_tab_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv = view.findViewById(R.id.rvStatistics)
        val layoutManager = LinearLayoutManager(view.context)
        rv.layoutManager = layoutManager
        adapter =
            StatisticAdapter(
                requireContext().applicationContext
            )
        rv.adapter = adapter
        rv.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        vm.stationStats.observe(this.viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> vm.onScrollStateChanged(false)
                    else -> vm.onScrollStateChanged(true)
                }
            }
        })
    }
}