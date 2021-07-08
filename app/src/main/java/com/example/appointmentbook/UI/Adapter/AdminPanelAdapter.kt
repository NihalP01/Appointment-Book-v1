package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.sample.SlotPendingDataItem

class AdminPanelAdapter : RecyclerView.Adapter<AdminPanelAdapter.AdminSlotViewHolder>() {

    var btnAcceptAdmin: ((position: Int, data: SlotPendingDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var btnRejectAdmin: ((position: Int, data: SlotPendingDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<SlotPendingDataItem> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminSlotViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.admin_notifications_items, parent, false)
        return AdminSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminSlotViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class AdminSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reqSlotDetails: TextView = itemView.findViewById(R.id.reqSlotDetails)
        private val reqName: TextView = itemView.findViewById(R.id.reqName)
        private val reqEmail: TextView = itemView.findViewById(R.id.reqEmail)
        private val reqBranch: TextView = itemView.findViewById(R.id.reqBranch)
        private val reqSemester: TextView = itemView.findViewById(R.id.reqSemester)
        private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.btnReject)

        @SuppressLint("SetTextI18n")
        fun bind(data: SlotPendingDataItem, position: Int) {
            reqSlotDetails.text =
                "Slot: ${data.slot_data.slot_id} (From ${data.slot_data.slot.timing.timing})"
            reqEmail.text = "Email: ${data.requested_by.email}"
            reqName.text = "Name: ${data.requested_by.name}"

            // TODO: 08/07/21 Will be added soon
//            reqBranch.text = "Branch: ${data.requested_by.student_details.branch}"
//            reqSemester.text = "Semester: ${data.requested_by.student_details.semester}"

            btnAccept.setOnClickListener {
                this@AdminPanelAdapter.btnAcceptAdmin?.invoke(position, data)
            }

            btnReject.setOnClickListener {
                this@AdminPanelAdapter.btnRejectAdmin?.invoke(position, data)
            }
        }
    }

}