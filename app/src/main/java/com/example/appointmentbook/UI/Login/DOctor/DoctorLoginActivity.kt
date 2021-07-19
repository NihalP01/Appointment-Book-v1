package com.example.appointmentbook.UI.Login.DOctor

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.DoctorSlots
import com.example.appointmentbook.utils.Utils
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.DOC_ID
import com.example.appointmentbook.utils.Utils.Companion.ROLE_KEY
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_EMAIL
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.setLogged
import com.example.appointmentbook.utils.Utils.Companion.subscribeToTopic
import com.example.appointmentbook.utils.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.android.synthetic.main.activity_user_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DoctorLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        supportActionBar?.hide()

        btnAdminLogin.setOnClickListener {
            adminLogin()
        }
    }

    private fun adminLogin() {
        if (adminEmail.text.toString().contains("@")) {
            if (adminEmail.text.toString()
                    .isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(adminEmail.text.toString())
                    .matches()
            ) {
                adminEmail.error = "Enter a valid email id or phone number"
                adminEmail.requestFocus()
                return
            }
        } else {
            if (adminEmail.text.toString()
                    .isEmpty() || !Patterns.PHONE.matcher(adminEmail.text.toString()).matches()
            ) {
                adminEmail.error = "Enter a valid email id or phone number"
                adminEmail.requestFocus()
                return
            }
        }

        if (adminPassword.text.toString().isEmpty()) {
            adminPassword.error = "Please enter your password"
            adminPassword.requestFocus()
            return
        }
        adminSigninProgress.visibility = View.VISIBLE
        btnAdminLogin.visibility = View.INVISIBLE

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.login(
                    adminEmail.text.toString(),
                    adminPassword.text.toString()
                )
                if (response.isSuccessful && response.body() != null) {
                    val role =
                        ApiAdapter.apiClient.role("${response.body()!!.type} ${response.body()!!.token}")

                    if (role.isSuccessful && role.body() != null) {
                        toast("Logged in successfully")
                        val sharedPreferences = getPreference()
                        val edit: SharedPreferences.Editor = sharedPreferences.edit()
                        val id = role.body()!!.data.user.id
                        edit.putInt(Utils.ID_KEY, id)
                        edit.putString(DOC_ID, role.body()!!.data.user.id.toString())
                        edit.putString(AUTH_TYPE, response.body()!!.type)
                        edit.putString(TOKEN_KEY, response.body()!!.token)
                        edit.putString(ROLE_KEY, role.body()!!.data.user.role)
                        edit.putString(USER_EMAIL, role.body()!!.data.user.email)
                        edit.putString(USER_NAME, role.body()!!.data.user.name)
                        edit.apply()
                        subscribeToTopic(role.body()!!.data.user.role)
                        subscribeToTopic(id.toString())
                        setLogged(true)
                        startActivity(
                            Intent(
                                this@DoctorLoginActivity,
                                DoctorSlots::class.java
                            )
                        )
                        finish()
                    } else {
                        toast(role.body().toString())
                    }
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }

        }
    }
}