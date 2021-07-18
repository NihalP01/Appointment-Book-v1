package com.example.appointmentbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.UI.DateTimePicker
import com.example.appointmentbook.UI.DoctorListActivity
import com.example.appointmentbook.UI.DoctorSlots
import com.example.appointmentbook.UI.Login.DOctor.DoctorLoginActivity
import com.example.appointmentbook.UI.Login.User.UserLoginActivity
import com.example.appointmentbook.utils.Utils.Companion.ROLE_KEY
import com.example.appointmentbook.utils.Utils.Companion.getFromPref
import com.example.appointmentbook.utils.Utils.Companion.getLogged
import com.example.appointmentbook.utils.Utils.Companion.startActivity
import com.example.appointmentbook.utils.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_startup.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
        supportActionBar?.hide()

        // TODO: 04/07/21 check if user logged in


        if (getLogged(context = this)) {
            //user logged in
            //read role and perform accordingly
            when (getFromPref(ROLE_KEY)) {
                "admin" -> {
                    //
                }
                "doctor" -> {
//                    startActivity(AdminPanelActivity::class.java)
                    startActivity(DoctorSlots::class.java)
                    finish()
                }
                else -> {
                    //if not teacher and admin its definitely student
                    startActivity(DoctorListActivity::class.java)
                    finish()
                }
            }
        }

        btnAdmin.setOnClickListener {
            startActivity(Intent(this, DoctorLoginActivity::class.java))
        }

//        btnTeacher.setOnClickListener {
//            startActivity(Intent(this, TeacherLoginActivity::class.java))
//
//        }
        btnUser.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
        }
    }
}
