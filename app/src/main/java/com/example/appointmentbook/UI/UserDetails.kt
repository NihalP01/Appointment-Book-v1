package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.MainActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.User.UserLoginActivity
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.example.appointmentbook.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.user_details.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserDetails : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    val token = getToken(TOKEN_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_details)

        userLogoutBtn.setOnClickListener {
            logoutWarn()
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.role("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    userInfo.text =
                        "Name: ${response.body()!!.data.user.name}\nPhone: ${response.body()!!.data.user.phone}\nEmail: ${response.body()!!.data.user.email}"
                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    private fun doUserLogout() {
        logout()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.putExtra("EXIT", true)
        startActivity(intent)
//        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun logoutWarn() {
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