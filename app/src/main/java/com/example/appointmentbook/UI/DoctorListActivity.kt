package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.DoctorListAdapter
import com.example.appointmentbook.UI.Login.User.UserLoginActivity
import com.example.appointmentbook.data.DoctorsListData
import com.example.appointmentbook.data.sample.SlotPendingDataItem
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_doctor_list.*

class DoctorListActivity: AppCompatActivity() {

    private val doctorPanelAdapter by lazy{
        DoctorListAdapter().apply {
            btnViewSlot = viewSlot
        }
    }

    private val viewSlot = { position: Int, data: DoctorsListData ->
        startActivity(Intent(this, SlotsActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        supportActionBar?.hide()

        btnUserLogout1.setOnClickListener {
            showAlert()
        }


    }

    private fun doUserLogout(){
        logout()
        startActivity(Intent(this, UserLoginActivity::class.java))
        finish()
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning !")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Do you want to logout?")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                doUserLogout()
            }
            .show()
    }

}


