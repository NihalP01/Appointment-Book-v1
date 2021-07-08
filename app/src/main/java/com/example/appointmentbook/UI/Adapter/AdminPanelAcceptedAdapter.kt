package com.example.appointmentbook.UI.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.sample.SlotPendingDataItem

class AdminPanelAcceptedAdapter :
    RecyclerView.Adapter<AdminPanelAcceptedAdapter.AcceptedViewHolder>() {

    var list: ArrayList<SlotPendingDataItem> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.req_accepted_items, parent, false)
        return AcceptedViewHolder(view)
    }

    override fun onBindViewHolder(holder: AcceptedViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class AcceptedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val acceptSlotDetails: TextView = itemView.findViewById(R.id.acceptSlotDetails)
        private val acceptName: TextView = itemView.findViewById(R.id.acceptName)
        private val acceptEmail: TextView = itemView.findViewById(R.id.acceptEmail)
        private val acceptBranch: TextView = itemView.findViewById(R.id.acceptBranch)
        private val acceptSemester: TextView = itemView.findViewById(R.id.acceptSemester)

        @SuppressLint("SetTextI18n")
        fun bind(data: SlotPendingDataItem) {
            acceptSlotDetails.text =
                "Slot: ${data.slot_data.slot_id} (From ${data.slot_data.slot.timing.timing})"
            acceptEmail.text = "Email: ${data.requested_by.email}"
            acceptName.text = "Name: ${data.requested_by.name}"
            // TODO: 08/07/21 Branch and semester will be added
        }
    }
}