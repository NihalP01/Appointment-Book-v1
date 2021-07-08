package com.example.appointmentbook.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.SlotsData

class SlotsAdapter : RecyclerView.Adapter<SlotsAdapter.myViewHolder>() {

    var btnBookSlot: ((position: Int, data: SlotsData) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<SlotsData> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slots_item, parent, false)
        return myViewHolder(view)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val slotNumber: TextView = itemView.findViewById(R.id.slotNumber)
        private val slotTiming: TextView = itemView.findViewById(R.id.slotTiming)
        private val btnBookSlots: Button = itemView.findViewById(R.id.btnBookSlot)

        fun bind(data: SlotsData, position: Int) {
            slotTiming.text = "Available form ${data.slot.timing.timing}"
            slotNumber.text = "Slot: ${data.id}"
            btnBookSlots.setOnClickListener {
                btnBookSlot?.invoke(position, data)
            }
        }
    }
}