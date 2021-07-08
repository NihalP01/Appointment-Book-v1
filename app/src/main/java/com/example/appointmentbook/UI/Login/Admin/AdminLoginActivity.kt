package com.example.appointmentbook.UI.Login.Admin

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.AdminPanelFragment
import com.example.appointmentbook.utils.Utils
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.ROLE_KEY
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_EMAIL
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.setLogged
import com.example.appointmentbook.utils.Utils.Companion.subscribeToTopic
import kotlinx.android.synthetic.main.activity_admin_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
        supportActionBar?.hide()

        btnAdminLogin.setOnClickListener {
            adminLogin()
        }
    }

    private fun adminLogin() {
        if (adminEmail.text.toString().isEmpty()) {
            adminEmail.error = "Please enter your email"
            adminEmail.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(adminEmail.text.toString()).matches()) {
            adminEmail.error = "Email id is not valis"
            adminEmail.requestFocus()
            return
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
                val response = ApiAdapter.apiClient.login(adminEmail.text.toString(), adminPassword.text.toString())
                if (response.isSuccessful && response.body() != null) {
                        val role =
                            ApiAdapter.apiClient.role("${response.body()!!.type} ${response.body()!!.token}")

                        if (role.isSuccessful && role.body() != null) {
                            val sharedPreferences = getPreference()
                            val edit: SharedPreferences.Editor = sharedPreferences.edit()
                            val id = role.body()!!.data.user.id
                            edit.putInt(Utils.ID_KEY, id)
                            edit.putString(AUTH_TYPE, response.body()!!.type)
                            edit.putString(TOKEN_KEY, response.body()!!.token)
                            edit.putString(ROLE_KEY, role.body()!!.data.user.role)
                            edit.putString(USER_EMAIL, role.body()!!.data.user.email)
                            edit.putString(USER_NAME, role.body()!!.data.user.name)
                            edit.apply()
                            subscribeToTopic(role.body()!!.data.user.role)
                            subscribeToTopic(id.toString())
                            setLogged(true)
                            startActivity(Intent(this@AdminLoginActivity, AdminPanelFragment::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@AdminLoginActivity, role.body().toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (e: Exception) {
                Toast.makeText(this@AdminLoginActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}