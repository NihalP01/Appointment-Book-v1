package com.example.medomind.UI

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.medomind.MainActivity
import com.example.medomind.Network.ApiAdapter
import com.example.medomind.R
import com.example.medomind.utils.Utils.Companion.AUTH_TYPE
import com.example.medomind.utils.Utils.Companion.TOKEN_KEY
import com.example.medomind.utils.Utils.Companion.getAuthType
import com.example.medomind.utils.Utils.Companion.getToken
import com.example.medomind.utils.Utils.Companion.logout
import com.example.medomind.utils.Utils.Companion.toast
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
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
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