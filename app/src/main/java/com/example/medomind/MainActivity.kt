package com.example.medomind

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medomind.UI.DoctorListActivity
import com.example.medomind.UI.DoctorSlots
import com.example.medomind.UI.Login.DOctor.DoctorLoginActivity
import com.example.medomind.UI.Login.User.UserLoginActivity
import com.example.medomind.utils.Utils.Companion.ROLE_KEY
import com.example.medomind.utils.Utils.Companion.getFromPref
import com.example.medomind.utils.Utils.Companion.getLogged
import com.example.medomind.utils.Utils.Companion.startActivity
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
