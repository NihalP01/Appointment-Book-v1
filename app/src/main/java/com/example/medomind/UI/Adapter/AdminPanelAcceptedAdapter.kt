package com.example.medomind.UI.Adapter

import android.annotation.SuppressLint
import android.icu.text.DateFormat.HOUR_MINUTE
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.medomind.R
import com.example.medomind.data.SlotBookRequests.SlotBookRequestsItem
import java.text.DateFormat

class AdminPanelAcceptedAdapter :
    RecyclerView.Adapter<AdminPanelAcceptedAdapter.AcceptedViewHolder>() {

    var list: ArrayList<SlotBookRequestsItem> = arrayListOf()
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

        @RequiresApi(Build.VERSION_CODES.N)
        @SuppressLint("SetTextI18n")
        fun bind(data: SlotBookRequestsItem) {
            val bookingStart =
                DateFormat.getDateInstance(HOUR_MINUTE.toInt()).format(data.slot.booking_start_time)
            acceptSlotDetails.text =
                "Slot: ${data.slot.id} (From $bookingStart )"
//            acceptEmail.text = "Email: ${data.requested_by.email}"
//            acceptName.text = "Name: ${data.requested_by.name}"
//            // TODO: 08/07/21 Branch and semester will be added
        }
    }

}