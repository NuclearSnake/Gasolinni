package com.neoproduction.gasolinni.ui.main.history

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neoproduction.gasolinni.R
import com.neoproduction.gasolinni.data.Refuel

class HistoryAdapter(private val context: Context) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var refuels: List<Refuel> = listOf()
    fun setData(newRefuels: List<Refuel>) {
        refuels = newRefuels
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_history_item, parent, false)

        return HistoryViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = refuels.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val refuel = refuels[position]
        holder.tvAmount.text = context.getString(R.string.placeh_amount, refuel.amount)
        holder.tvPrice.text =
            context.getString(R.string.placeh_price, refuel.price.toDouble() / 100)

        holder.tvGpsLocation.text = if (refuel.stationAddress.gps.isBlank())
            ""
        else
            refuel.stationAddress.gps

        holder.tvTextAddress.text = if (refuel.stationAddress.textAddress.isBlank())
            "N/A"
        else
            refuel.stationAddress.textAddress

        val dateTime = DateUtils.formatDateTime(
            context,
            refuel.timestamp,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME
        )
        holder.tvDatetime.text = dateTime

        holder.tvSupplier.text = refuel.supplier
        holder.tvFuel.text = refuel.fuel
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvGpsLocation: TextView = view.findViewById(
            R.id.tvGpsLocation
        )
        val tvTextAddress: TextView = view.findViewById(
            R.id.tvTextAddress
        )
        val tvSupplier: TextView = view.findViewById(
            R.id.tvSupplier
        )
        val tvFuel: TextView = view.findViewById(
            R.id.tvFuel
        )
        val tvDatetime: TextView = view.findViewById(
            R.id.tvDatetime
        )
        val tvAmount: TextView = view.findViewById(
            R.id.tvAmount
        )
        val tvPrice: TextView = view.findViewById(
            R.id.tvPrice
        )
    }
}