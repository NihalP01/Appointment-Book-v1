package com.example.appointmentbook.UI.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.sample.SlotPendingDataItem

class AdminPanelRejectedAdapter: RecyclerView.Adapter<AdminPanelRejectedAdapter.RejectedViewHolder>() {

    var list: ArrayList<SlotPendingDataItem> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminPanelRejectedAdapter.RejectedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.req_rejected_items, parent, false)
        return RejectedViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminPanelRejectedAdapter.RejectedViewHolder, position: Int) {
            holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class RejectedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val rejectSlotDetails: TextView = itemView.findViewById(R.id.rejectSlotDetails)
        private val rejectName: TextView = itemView.findViewById(R.id.rejectName)
        private val rejectEmail: TextView = itemView.findViewById(R.id.rejectEmail)
        private val rejectBranch: TextView = itemView.findViewById(R.id.rejectBranch)
        private val rejectSemester: TextView = itemView.findViewById(R.id.rejectSemester)
        @SuppressLint("SetTextI18n")
        fun bind(data: SlotPendingDataItem){
            rejectSlotDetails.text =
                "Slot: ${data.slot_data.slot_id} (From ${data.slot_data.slot.timing.timing})"
            rejectEmail.text = "Email: ${data.requested_by.email}"
            rejectName.text = "Name: ${data.requested_by.name}"
            // TODO: 08/07/21 Branch and semester will be added
        }
    }


}