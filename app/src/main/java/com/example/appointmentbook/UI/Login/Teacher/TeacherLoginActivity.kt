package com.example.appointmentbook.UI.Login.Teacher

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.DoctorPanelActivity
import com.example.appointmentbook.UI.Signup.Teacher.TeacherSignUpActivity
import kotlinx.android.synthetic.main.activity_teacher_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeacherLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_login)
        supportActionBar?.hide()
        checkStatus()
        btnTeacherLogin.setOnClickListener {
//            doValidation()
        }

        teacherSignup.setOnClickListener {
            startActivity(Intent(this, TeacherSignUpActivity::class.java))
        }
    }

    @SuppressLint("WrongConstant")
    private fun doValidation() {
        if (teacherEmail.text.toString().isEmpty()) {
            teacherEmail.error = "Please enter your email ID"
            teacherEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(teacherEmail.text.toString()).matches()) {
            teacherEmail.error = "Email ID is not valid"
            teacherEmail.requestFocus()
            return
        }
        if (teacherPassword.text.toString().isEmpty()) {
            teacherPassword.error = "Please enter your password"
            teacherPassword.requestFocus()
            return
        }

        teacherSigninProgress.visibility = View.VISIBLE
        btnTeacherLogin.visibility = View.INVISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.login(
                    teacherEmail.text.toString(),
                    teacherPassword.text.toString()
                )
                if (response.isSuccessful && response.body() != null) {
//                    val getPref: SharedPreferences =
//                        getSharedPreferences("tokenSharedPref", MODE_APPEND)
//                    val token = getPref.getString("token", "")
//                    val sharedPreferences: SharedPreferences =
//                        getSharedPreferences("adminSharedPref", MODE_PRIVATE)
//                    val edit: SharedPreferences.Editor = sharedPreferences.edit()
//                    val role = ApiAdapter.apiClient.role("Bearer $token")
//                    edit.putString("role", role.body()!!.data.user.role)
//                    edit.putBoolean("login", true)
//                    edit.apply()

                    startActivity(Intent(this@TeacherLoginActivity, DoctorPanelActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@TeacherLoginActivity,
                        response.message().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TeacherLoginActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }

    @SuppressLint("WrongConstant")
    private fun checkStatus() {
        val getPref: SharedPreferences = getSharedPreferences("adminSharedPref", MODE_APPEND)
        val login = getPref.getBoolean("login", false)
        val role = getPref.getString("role", "")
        Log.d("myRole", role.toString())
        if (login) {
            if (role == "admin") {
                startActivity(Intent(this, DoctorPanelActivity::class.java))
            }
        } else {
            Toast.makeText(this, "Login to continue", Toast.LENGTH_SHORT).show()
        }
    }
}
