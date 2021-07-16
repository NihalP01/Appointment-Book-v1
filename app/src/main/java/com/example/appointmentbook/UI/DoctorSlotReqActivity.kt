package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.DOctor.DoctorLoginActivity
import com.example.appointmentbook.data.BookReqData.BookReqDataItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.getUserName
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_doc_slots.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DoctorSlotReqActivity : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    private val token = getToken(TOKEN_KEY)
    private val adminPanelAdapter by lazy {
        DoctorSlotReqAdapter().apply {
            btnAcceptDoc = btnAcceptClick
            btnRejectDoc = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: BookReqDataItem ->
        actionAccept(data, position)
    }

    private val btnRejectClick = { position: Int, data: BookReqDataItem ->
        showAlert(data, position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_slots)
        supportActionBar?.hide()
        getData()

        btnAdminLogout0?.setOnClickListener {
            logout()
            startActivity(Intent(this, DoctorLoginActivity::class.java))
            finish()
        }

        notificationRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adminPanelAdapter
            setHasFixedSize(true)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.docSlotRec("$type $token")
                Log.d("myTag", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    Log.d("myTag", response.body().toString())
                    adminPanelAdapter.list = response.body() as ArrayList<BookReqDataItem>
                    adminPanelAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@DoctorSlotReqActivity,
                        response.body().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorSlotReqActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun actionAccept(data: BookReqDataItem, position: Int) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction("$type $token", "accepted", data.bookings[position].id)
                Log.d("myTag", res.body().toString())
                if (res.isSuccessful && res.body() != null) {
                    Toast.makeText(
                        this@DoctorSlotReqActivity,
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

    private fun actionReject(data: BookReqDataItem, position: Int) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction("$type $token", "rejected", data.bookings[position].id)
                if (res.isSuccessful && res.body() != null) {
                    Toast.makeText(
                        this@DoctorSlotReqActivity,
                        "Request has been rejected",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("myTag", res.message().toString())
                }
            } catch (e: Exception) {
                Toast.makeText(this@DoctorSlotReqActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //show a confirmation alert dialog
    private fun showAlert(data: BookReqDataItem, position: Int) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure to reject ?")
            .setMessage("On confirming, the Book request of with slot number  will be rejected. will receive a notification of the same")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                actionReject(data, position)
            }
            .show()
    }

    private fun getData() {
        val name = getUserName(USER_NAME)
        adminName.text = name
    }
}