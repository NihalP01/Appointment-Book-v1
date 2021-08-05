package com.example.medomind.UI

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medomind.R
import com.example.medomind.data.SlotsbyReqIdData.SlotsByReqIdItem
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DoctorSlotReqAdapter : RecyclerView.Adapter<DoctorSlotReqAdapter.AdminSlotViewHolder>() {

    var btnAcceptDoc: ((position: Int, data: SlotsByReqIdItem) -> Unit)? = null
        set(value) = run { field = value }

    var btnRejectDoc: ((position: Int, data: SlotsByReqIdItem) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<SlotsByReqIdItem> = arrayListOf()
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
        private val requestedAt: TextView = itemView.findViewById(R.id.requestedAt)
        private val btnAccept: Button = itemView.findViewById(R.id.reqBtnAccept)
        private val btnReject: Button = itemView.findViewById(R.id.reqBtnReject)
        private val btnStatus: Button = itemView.findViewById(R.id.btnStatus)

        @SuppressLint("SetTextI18n")
        fun bind(data: SlotsByReqIdItem, position: Int) {
            reqSlotDetails.text =
                "Slot: ${data.slot_id}"
            reqName.text = "Name: ${data.requested_user.name}"
            reqEmail.text = "Email: ${data.requested_user.email}"
            reqPhone.text = "Phone: ${data.requested_user.phone}"
            requestedAt.text = "Requested At: ${timeFormatter(data.created_at)}"

            if (data.status == "rejected") {
                btnStatus.text = "Rejected"
                btnStatus.visibility = View.VISIBLE
                btnReject.visibility = View.INVISIBLE
                btnAccept.visibility = View.INVISIBLE
            } else if (data.status == "accepted") {
                btnStatus.visibility = View.VISIBLE
                btnStatus.text = "Accepted"
                btnAccept.visibility = View.INVISIBLE
                btnReject.visibility = View.INVISIBLE
            } else {
                btnAccept.setOnClickListener {
                    this@DoctorSlotReqAdapter.btnAcceptDoc?.invoke(position, data)
                }

                btnReject.setOnClickListener {
                    this@DoctorSlotReqAdapter.btnRejectDoc?.invoke(position, data)
                }
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