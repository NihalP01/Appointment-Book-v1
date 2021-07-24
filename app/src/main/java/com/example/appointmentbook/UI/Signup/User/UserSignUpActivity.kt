package com.example.appointmentbook.UI.Signup.User

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.User.UserLoginActivity
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserSignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        supportActionBar?.hide()
        val btnUserSignUp = findViewById<Button>(R.id.btnUserSignup)
        btnUserSignUp.setOnClickListener {
            validateInput()
        }

        btnHaveAccount.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
        }
    }

    private fun validateInput() {
        if (userName.text.toString().isEmpty()) {
            userName.error = "Please enter your name"
            userName.requestFocus()
            return
        }
        if (userPhoneNumber.text.toString()
                .isEmpty() || !Patterns.PHONE.matcher(userPhoneNumber.text.toString()).matches()
        ) {
            userPhoneNumber.error = "Please enter a valid phone number"
            userPhoneNumber.requestFocus()
            return
        }

        if (userSignupEmail.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(
                userSignupEmail.text.toString()
            ).matches()
        ) {
            userSignupEmail.error = "Please enter a valid email ID"
            userSignupEmail.requestFocus()
            return
        }

        if (userSignupPass.text.toString().isEmpty()) {
            userSignupPass.error = "Please enter your password"
            userSignupPass.requestFocus()
            return
        }
        if (userSignupPass.text.toString() != userConfirmPass.text.toString()) {
            userConfirmPass.error = "Password did not match"
            userConfirmPass.requestFocus()
            return
        }

        btnUserSignup.visibility = View.INVISIBLE
        signupProgressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val userPhoneNumber = userPhoneNumber.text.toString()
                val userName = userName.text.toString()

                val response = ApiAdapter.apiClient.signUpStudent(
                    userName,
                    userSignupEmail.text.toString(),
                    userSignupPass.text.toString(),
                    userPhoneNumber
                )

                if (response.isSuccessful && response.body() != null) {
                    val sharedPreferences: SharedPreferences =
                        getSharedPreferences("tokenSharedPref", MODE_PRIVATE)
                    val edit: SharedPreferences.Editor = sharedPreferences.edit()
                    edit.putString("role", "student")
//                    edit.putString("userBranch", userPhoneNumber)
                    edit.putString("userName", userName)
                    edit.apply()
                    startActivity(Intent(this@UserSignUpActivity, UserLoginActivity::class.java))
                    finish()
                } else {
                    btnUserSignup.visibility = View.VISIBLE
                    signupProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@UserSignUpActivity,
                        response.message().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                btnUserSignup.visibility = View.VISIBLE
                signupProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this@UserSignUpActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

}



