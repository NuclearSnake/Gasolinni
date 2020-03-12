package com.neoproduction.gasolinni.ui.main.history

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

class HistoryFragment : Fragment() {
    private val vm: MainViewModel by activityViewModels()

    private lateinit var rv: RecyclerView
    private lateinit var adapter: HistoryAdapter

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
        adapter =
            HistoryAdapter(
                requireContext().applicationContext
            )
        rv.adapter = adapter
        rv.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))

        vm.refuels.observe(this.viewLifecycleOwner, Observer { list ->
            adapter.setData(list)
        })
    }
}

