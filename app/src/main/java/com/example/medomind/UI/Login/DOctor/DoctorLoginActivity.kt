package com.example.medomind.UI.Login.DOctor

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.medomind.Network.ApiAdapter
import com.example.medomind.R
import com.example.medomind.UI.DoctorSlots
import com.example.medomind.utils.Utils
import com.example.medomind.utils.Utils.Companion.AUTH_TYPE
import com.example.medomind.utils.Utils.Companion.DOC_ID
import com.example.medomind.utils.Utils.Companion.ROLE_KEY
import com.example.medomind.utils.Utils.Companion.TOKEN_KEY
import com.example.medomind.utils.Utils.Companion.USER_EMAIL
import com.example.medomind.utils.Utils.Companion.USER_NAME
import com.example.medomind.utils.Utils.Companion.fancyToastFail
import com.example.medomind.utils.Utils.Companion.fancyToastSuccess
import com.example.medomind.utils.Utils.Companion.getPreference
import com.example.medomind.utils.Utils.Companion.setLogged
import com.example.medomind.utils.Utils.Companion.subscribeToTopic
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_doc_login.*
import kotlinx.android.synthetic.main.activity_user_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DoctorLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_login)
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

        GlobalScope.launch(Dispatchers.Main) {
            try {
                adminSigninProgress.visibility = View.VISIBLE
                btnAdminLogin.visibility = View.INVISIBLE
                val response = ApiAdapter.apiClient.login(
                    adminEmail.text.toString(),
                    adminPassword.text.toString()
                )
                if (response.isSuccessful && response.body() != null) {
                    val role =
                        ApiAdapter.apiClient.role("${response.body()!!.type} ${response.body()!!.token}")
                    Log.d("noice", role.raw().toString())
                    if (role.isSuccessful && role.body() != null) {
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
                        if (role.body()!!.data.user.role == "doctor") {
                            subscribeToTopic(role.body()!!.data.user.role)
                            subscribeToTopic(id.toString())
                            setLogged(true)
                            startActivity(Intent(this@DoctorLoginActivity, DoctorSlots::class.java))
                            finish()
                            fancyToastSuccess("Successfully logged in")
                        } else {
                            showAlert()
                            adminSigninProgress.visibility = View.INVISIBLE
                            btnAdminLogin.visibility = View.VISIBLE
                        }
                    } else {
                        adminSigninProgress.visibility = View.INVISIBLE
                        btnAdminLogin.visibility = View.VISIBLE
                        Log.d("myTag", role.message().toString())
                        //fancyToastFail("Login failed ! Check your credentials")
                    }
                }else{
                    adminSigninProgress.visibility = View.INVISIBLE
                    btnAdminLogin.visibility = View.VISIBLE
                    fancyToastFail("Login failed ! Check your credentials")
                }
            } catch (e: Exception) {
                adminSigninProgress.visibility = View.INVISIBLE
                btnAdminLogin.visibility = View.VISIBLE
                //toast(e.message.toString())
                Log.d("myTag", e.message.toString())
            }
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Login Failed !")
            .setIcon(R.drawable.ic_warning)
            .setMessage("You are using patient account. Please login from patient section.")
            .setPositiveButton("Ok") { dialog, which ->
                finish()
            }
            .show()
    }
}