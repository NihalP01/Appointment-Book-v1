package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.BookReqData.BookReqDataItem

class DoctorSlotReqAdapter : RecyclerView.Adapter<DoctorSlotReqAdapter.AdminSlotViewHolder>() {

    var btnAcceptDoc: ((position: Int, data: BookReqDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var btnRejectDoc: ((position: Int, data: BookReqDataItem) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<BookReqDataItem> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminSlotViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.doc_slot_req_items, parent, false)
        return AdminSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminSlotViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class AdminSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val reqSlotDetails: TextView = itemView.findViewById(R.id.reqSlotDetails)
        private val reqName: TextView = itemView.findViewById(R.id.reqUserName)
        private val reqEmail: TextView = itemView.findViewById(R.id.reqUserEmail)
        private val reqPhone: TextView = itemView.findViewById(R.id.reqUserPhone)
        private val reqSemester: TextView = itemView.findViewById(R.id.requestedAt)
        private val btnAccept: Button = itemView.findViewById(R.id.reqBtnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.reqBtnReject)

        @SuppressLint("SetTextI18n")
        fun bind(data: BookReqDataItem, position: Int) {
            reqSlotDetails.text =
                "Slot: ${data.id} (From ${data.booking_start_time})"
            reqName.text = "Name: ${data.bookings[position].requested_user[position].name}"
            reqEmail.text = "Email: ${data.bookings[position].requested_user[position].email}"


            btnAccept.setOnClickListener {
                this@DoctorSlotReqAdapter.btnAcceptDoc?.invoke(position, data)
            }

            btnReject.setOnClickListener {
                this@DoctorSlotReqAdapter.btnRejectDoc?.invoke(position, data)
            }
        }
    }

}