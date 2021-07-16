package com.example.appointmentbook.UI.Signup.Teacher

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.DoctorSlotReqActivity
import kotlinx.android.synthetic.main.activity_teacher_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TeacherSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_signup)

        btnAdminSignup.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        if (adminSignupName.text.toString().isEmpty()) {
            adminSignupName.error = "Please enter your name"
            adminSignupName.requestFocus()
            return
        }
        if (adminSignupEmail.text.toString().isEmpty()) {
            adminSignupEmail.error = "Please enter your email"
            adminSignupEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(adminSignupEmail.text.toString()).matches()) {
            adminSignupEmail.error = "Email ID is not valid"
            adminSignupEmail.requestFocus()
            return
        }
        if (adminSignupPass.text.toString().isEmpty()) {
            adminSignupPass.error = "Enter your password"
            adminSignupPass.requestFocus()
            return
        }
        if (adminSignupPass.text.toString() != adminSignupConfirmPass.text.toString()) {
            adminSignupConfirmPass.error = "Password did not match"
            adminSignupConfirmPass.requestFocus()
            return
        }
        if (adminUniqueId.text.toString().isEmpty()) {
            adminUniqueId.error = "Please enter the Unique ID"
            adminUniqueId.requestFocus()
            return
        }
        if (adminUniqueId.text.toString() != "uniqueID") {
            Toast.makeText(this, "You have entered invalid Unique ID", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = ApiAdapter.apiClient.signUpTeacher(
                        adminSignupName.text.toString(),
                        adminSignupEmail.text.toString(),
                        adminSignupPass.text.toString()
                    )
                    if (response.isSuccessful && response.body() != null) {
                        startActivity(Intent(this@TeacherSignUpActivity, DoctorSlotReqActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@TeacherSignUpActivity, response.body().toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@TeacherSignUpActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}