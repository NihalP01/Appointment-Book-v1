package com.example.appointmentbook.UI.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        fun bind(bookData: BookRequestDataItem) {
            slotTime.text = bookData.slot_data.slot.created_at
            bookSlotNumber.text = bookData.slot_data.slot.name
            bookedTeacherName.text = bookData.slot_data.teacher.name
            bookStatus.text = bookData.status
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