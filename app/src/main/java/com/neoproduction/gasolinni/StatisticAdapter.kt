package com.neoproduction.gasolinni

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neoproduction.gasolinni.data.StationStats

class StatisticAdapter(private val stations: List<StationStats>, private val context: Context) :
    RecyclerView.Adapter<StatisticAdapter.StatisticViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_statistics_item, parent, false)

        return StatisticViewHolder(view)
    }

    override fun getItemCount(): Int = stations.size

    override fun onBindViewHolder(holder: StatisticViewHolder, position: Int) {
        val station = stations[position]
        holder.tvTextAddress.text = station.text_address
        holder.tvGpsLocation.text = if (station.gps.isBlank()) "" else station.gps
        holder.tvAmount.text = context.getString(R.string.placeh_amount, station.amount)
        holder.tvTotal.text =
            context.getString(R.string.placeh_price, station.total.toDouble() / 100)
    }

    class StatisticViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGpsLocation: TextView = view.findViewById(R.id.tvGpsLocation)
        val tvTextAddress: TextView = view.findViewById(R.id.tvTextAddress)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
    }
}