package com.example.appointmentbook.UI.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.R
import com.example.appointmentbook.data.BookRequestData.BookRequestDataItem

class BookReqAdapter(
    private val BookReqData: ArrayList<BookRequestDataItem> = arrayListOf()
) : RecyclerView.Adapter<BookReqAdapter.BookReqViewHolder>() {

    class BookReqViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val slotTime: TextView = itemView.findViewById(R.id.bookSlotTime)
        private val bookSlotNumber: TextView = itemView.findViewById(R.id.bookSlotNumber)
        private val bookedTeacherName: TextView = itemView.findViewById(R.id.bookedTeacherName)
        private val bookStatus: TextView = itemView.findViewById(R.id.bookStatus)
        private val slotCard: CardView = itemView.findViewById(R.id.slotCard)

        @SuppressLint("SetTextI18n")
        fun bind(bookData: BookRequestDataItem) {
            slotTime.text = "From ${bookData.slot_data.slot.timing.timing}"
            bookSlotNumber.text = "Slot: ${bookData.slot_data.slot.name}"
            bookedTeacherName.text = "Teacher Name: ${bookData.slot_data.teacher.name}"
            bookStatus.text = "Status: ${bookData.status}"
            if (bookData.status == "rejected") {
                slotCard.setCardBackgroundColor(Color.rgb(239, 83, 80))
            } else {
                slotCard.setCardBackgroundColor(Color.rgb(102, 187, 106))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookReqViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.book_req_item, parent, false)
        return BookReqViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookReqViewHolder, position: Int) {
        val list: BookRequestDataItem = BookReqData[position]

        return holder.bind(BookReqData[position])
    }

    override fun getItemCount() = BookReqData.size

}