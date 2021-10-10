package com.example.medomind.UI.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medomind.R
import com.example.medomind.data.DoctorListData.DoctorsListData

class DoctorListAdapter : RecyclerView.Adapter<DoctorListAdapter.DoctorViewHolder>() {

    var btnViewSlot: ((position: Int, data: DoctorsListData) -> Unit)? = null
        set(value) = run { field = value }

    var list: ArrayList<DoctorsListData> = arrayListOf()
        set(value) = run {
            field = value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DoctorViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.doctor_list_items, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorListAdapter.DoctorViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

    inner class DoctorViewHolder(itmeView: View) : RecyclerView.ViewHolder(itmeView) {
        private val doctorName: TextView = itmeView.findViewById(R.id.doctorName)
        private val doctorDegree: TextView? = itemView.findViewById(R.id.doctorDegree)
        private val doctorConsultant: TextView? = itemView.findViewById(R.id.doctorConsultant)
        private val viewSlot: Button = itmeView.findViewById(R.id.doctorViewSlots)

        fun bind(data: DoctorsListData, position: Int) {
            doctorName.text = "${data.name}"
//            doctorDegree!!.text = "Works At: ${data.details.details.works_at}"
//            doctorConsultant!!.text = "Speciality: ${data.details.details.speciality}"
            viewSlot.setOnClickListener {
                this@DoctorListAdapter.btnViewSlot?.invoke(position, data)
            }
        }
    }

}