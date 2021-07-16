package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.Admin.AdminLoginActivity
import com.example.appointmentbook.data.DoctorSlotsReq.DoctorSlotsReqItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.getUserName
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_admin_panel.*
import kotlinx.android.synthetic.main.admin_notifications_items.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DoctorPanelActivity : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    private val token = getToken(TOKEN_KEY)
    private val adminPanelAdapter by lazy {
        DoctorPanelAdapter().apply {
            btnAcceptAdmin = btnAcceptClick
            btnRejectAdmin = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: DoctorSlotsReqItem ->
        actionAccept(data)
    }

    private val btnRejectClick = { position: Int, data: DoctorSlotsReqItem ->
        showAlert(data)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        supportActionBar?.hide()
        getData()
        btnAdminLogout.setOnClickListener {
            val sharedPreferences = getPreference()
            val edit: SharedPreferences.Editor = sharedPreferences.edit()
            edit.putBoolean("login", false)
            edit.apply()
            startActivity(Intent(this, AdminLoginActivity::class.java))
            finish()
        }

        notificationRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adminPanelAdapter
            setHasFixedSize(true)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.docReqList("$type $token")
                Log.d("myTag", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    Log.d("myTag", response.body().toString())
                    adminPanelAdapter.list = response.body() as ArrayList<DoctorSlotsReqItem>
                    adminPanelAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@DoctorPanelActivity,
                        response.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorPanelActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun actionAccept(data: DoctorSlotsReqItem) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction("$type $token", "accepted", data.id)
                Log.d("myTag", res.body().toString())
                if (res.isSuccessful && res.body() != null) {
                    Toast.makeText(
                        this@DoctorPanelActivity,
                        "Request has been accepted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("myTag", res.message().toString())
                }
            } catch (e: Exception) {
                Log.d("myTag", e.message.toString())
            }
        }
    }

    private fun actionReject(data: DoctorSlotsReqItem) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction("$type $token", "rejected", data.id)
                if (res.isSuccessful && res.body() != null) {
                    Toast.makeText(
                        this@DoctorPanelActivity,
                        "Request has been rejected",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("myTag", res.message().toString())
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorPanelActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //show a confirmation alert dialog
    private fun showAlert(data: DoctorSlotsReqItem) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure to reject ?")
            .setMessage("On confirming, the Book request of with slot number  will be rejected. will receive a notification of the same")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                actionReject(data)
            }
            .show()
    }

    private fun getData() {
        val name = getUserName(USER_NAME)
        adminName.text = name
    }
}