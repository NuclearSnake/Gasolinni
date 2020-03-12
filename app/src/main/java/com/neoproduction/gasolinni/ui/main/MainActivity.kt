package com.neoproduction.gasolinni.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.neoproduction.gasolinni.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val vm: MainViewModel by viewModels()

    private lateinit var mainFragmentStateAdapter: MainFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainFragmentStateAdapter =
            MainFragmentStateAdapter(this)
        pager.adapter = mainFragmentStateAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = mainFragmentStateAdapter.fragmentNames[position]
        }.attach()

        fab.setOnClickListener { vm.onFabClick(this) }
    }
}

