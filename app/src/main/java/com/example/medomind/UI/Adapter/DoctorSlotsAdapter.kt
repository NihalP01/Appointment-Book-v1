package com.example.medomind.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medomind.R
import com.example.medomind.data.SlotsData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DoctorSlotsAdapter : RecyclerView.Adapter<DoctorSlotsAdapter.AdminSlotViewHolder>() {

    var btnSlot: ((position: Int, data: SlotsData) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<SlotsData> = arrayListOf()
        set(value) = run { field = value }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminSlotViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.doc_slots_items, parent, false)
        return AdminSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminSlotViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class AdminSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val slotId: TextView = itemView.findViewById(R.id.reqSlotId)
        private val slotBookingStart: TextView = itemView.findViewById(R.id.reqBookingStart)
        private val slotCapacity: TextView = itemView.findViewById(R.id.reqSlotCapacity)
        private val slotBookingEnd: TextView = itemView.findViewById(R.id.reqBookingEnd)
        private val slotAvailability: TextView = itemView.findViewById(R.id.reqAvailability)
        private val btnViewRequests: Button = itemView.findViewById(R.id.btnViewReqs)

        @SuppressLint("SetTextI18n")
        fun bind(data: SlotsData, position: Int) {
            slotId.text = "Slot: ${data.id}"
            slotBookingStart.text = "Starts At: ${timeFormatter(data.booking_start_time)}"
            slotCapacity.text = "Capacity: ${data.capacity}"
            slotBookingEnd.text = "Ends At: ${timeFormatter(data.booking_end_time)}"

            if (data.capacity == data.current_filled) {
                slotAvailability.text = "Availability: Unavailable"
            } else {
                slotAvailability.text = "Availability: Available"
            }
            btnViewRequests.setOnClickListener {
                btnSlot?.invoke(position, data)
            }
        }

        private fun timeFormatter(inputDate: String): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US)
            val parseDate = sdf.parse(inputDate)
            val formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
            return formatter.format(parseDate!!)
        }


    }

}