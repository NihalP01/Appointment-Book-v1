package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.AlllBookReq.AllBookReqDataItem

class AdminPanelAdapter : RecyclerView.Adapter<AdminPanelAdapter.AdminSlotViewHolder>() {

    var btnAcceptAdmin: ((position: Int, data: AllBookReqDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var btnRejectAdmin: ((position: Int, data: AllBookReqDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<AllBookReqDataItem> = arrayListOf()
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
        private val reqPhone: TextView = itemView.findViewById(R.id.reqBranch)
        private val reqSemester: TextView = itemView.findViewById(R.id.reqSemester)
        private val btnAccept: Button = itemView.findViewById(R.id.btnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.btnReject)

        @SuppressLint("SetTextI18n")
        fun bind(data: AllBookReqDataItem, position: Int) {
            reqSlotDetails.text =
                "Slot: ${data.slot.id} (From ${data.slot.booking_start_time})"
            reqEmail.text = "Email: ${data.slot.associated_to.email}"
            reqName.text = "Name: ${data.slot.associated_to.name}"
            reqPhone.text = "Phone: ${data.slot.associated_to.phone}"
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