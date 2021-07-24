package com.example.appointmentbook.UI.Login.User

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.DoctorListActivity
import com.example.appointmentbook.UI.Signup.User.UserSignUpActivity
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.ID_KEY
import com.example.appointmentbook.utils.Utils.Companion.ROLE_KEY
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_EMAIL
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.setLogged
import com.example.appointmentbook.utils.Utils.Companion.subscribeToTopic
import kotlinx.android.synthetic.main.activity_user_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)
        supportActionBar?.hide()
        // checkStatus()
        val btnLogin: Button = findViewById(R.id.btnUserLogin)
        val btnSignup: TextView = findViewById(R.id.btnUserSignup)

        btnSignup.setOnClickListener {
            startActivity(Intent(this, UserSignUpActivity::class.java))
        }

        btnLogin.setOnClickListener {
            userLogin()
        }

    }

    private fun userLogin() {
        if (userEmail.text.toString().contains("@")) {
            if (userEmail.text.toString()
                    .isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString())
                    .matches()
            ) {
                userEmail.error = "Please enter a valid email or phone number"
                userEmail.requestFocus()
                return
            }
        } else {
            if (userEmail.text.toString()
                    .isEmpty() || !Patterns.PHONE.matcher(userEmail.text.toString()).matches()
            ) {
                userEmail.error = "Please enter a valid email or phone number"
                userEmail.requestFocus()
                return
            }
        }

        if (userPassword.text.toString().isEmpty()) {
            userPassword.error = "Please enter your password"
            userPassword.requestFocus()
            return
        }
        btnUserLogin.visibility = View.INVISIBLE
        loginProgressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.Default) {
            try {
                val response = ApiAdapter.apiClient.login(
                    userEmail.text.toString(),
                    userPassword.text.toString()
                )
                if (response.isSuccessful && response.body() != null) {
                    val role =
                        ApiAdapter.apiClient.role("${response.body()!!.type} ${response.body()!!.token}")

                    if (role.isSuccessful && role.body() != null) {
                        /*val sharedPreferences: SharedPreferences =
                            getSharedPreferences("tokenSharedPref", MODE_PRIVATE)*/
                        val sharedPreferences = getPreference()
                        val edit: SharedPreferences.Editor = sharedPreferences.edit()
                        val id = role.body()!!.data.user.id
                        edit.putInt(ID_KEY, id)
                        edit.putString(AUTH_TYPE, response.body()!!.type)
                        edit.putString(TOKEN_KEY, response.body()!!.token)
                        edit.putString(ROLE_KEY, role.body()!!.data.user.role)
                        edit.putString(USER_EMAIL, role.body()!!.data.user.email)
                        edit.putString(USER_NAME, role.body()!!.data.user.name)
                        edit.apply()
                        subscribeToTopic(role.body()!!.data.user.role)
                        subscribeToTopic(id.toString())
                        setLogged(true)
                        val intent = Intent(this@UserLoginActivity, DoctorListActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.d("myTag", response.body().toString())
                    }

                } else {
                    btnUserLogin.visibility = View.VISIBLE
                    loginProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(
                        this@UserLoginActivity,
                        response.message().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                btnUserLogin.visibility = View.VISIBLE
                loginProgressBar.visibility = View.INVISIBLE
                Toast.makeText(this@UserLoginActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


//    private fun checkStatus() {
//        val getPref: SharedPreferences = getSharedPreferences("app_pref", MODE_APPEND)
//        val login = getPref.getBoolean("login", false)
//        val role = getPref.getString("role", "")
//        if (role == "student") {
//            if (login) {
//                startActivity(Intent(this, SlotsActivity::class.java))
//                finish()
//            } else {
//                startActivity(Intent(this, AdminPanelActivity::class.java))
//                finish()
//            }
//        }
}
