package com.example.appointmentbook.UI.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.SlotsData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

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
        private val slotStatus: TextView = itemView.findViewById(R.id.slotStatus)
        private val btnBookSlots: Button = itemView.findViewById(R.id.btnBookSlot)

        @SuppressLint("SetTextI18n")
        fun bind(data: SlotsData, position: Int) {
            slotTiming.text =
                "Available form: ${timeFormatter(data.booking_start_time)} to ${timeFormatter(data.booking_end_time)}"
            slotNumber.text = "Slot: ${data.id}"
            if (data.capacity == data.current_filled) {
                slotStatus.text = "Status: Already booked"
                markButtonDisable(btnBookSlots)
            } else {
                slotStatus.text = "Status: Available"
            }

            btnBookSlots.setOnClickListener {
                btnBookSlot?.invoke(position, data)
            }
        }

        private fun timeFormatter(inputDate: String): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
            val parseDate = sdf.parse(inputDate)
            val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return formatter.format(parseDate!!)
        }

        private fun markButtonDisable(button: Button) {
            button.isEnabled = false
            button.setTextColor(Color.WHITE)
            button.setBackgroundColor(Color.GRAY)
        }
    }
}