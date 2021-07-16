package com.example.appointmentbook.UI

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.data.SlotsbyReqIdData.SlotsByReqIdItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.getUserName
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_slot_req_by_id.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DoctorSlotReqActivity : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    private val token = getToken(TOKEN_KEY)

    private val doctorPanelAdapter by lazy {
        DoctorSlotReqAdapter().apply {
            btnAcceptDoc = btnAcceptClick
            btnRejectDoc = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: SlotsByReqIdItem ->
        actionAccept(data, position)
    }

    private val btnRejectClick = { position: Int, data: SlotsByReqIdItem ->
        showAlert(data, position)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot_req_by_id)

        val intent = Intent()
        val id = intent.getStringExtra("id")

        supportActionBar?.hide()

        recSlotReqById.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = doctorPanelAdapter
            setHasFixedSize(true)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.slotsByID("$type $token", 22)
                if (response.isSuccessful && response.body() != null) {
                    doctorPanelAdapter.list = response.body() as ArrayList<SlotsByReqIdItem>
                    doctorPanelAdapter.notifyDataSetChanged()
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

    private fun actionAccept(data: SlotsByReqIdItem, position: Int) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction(
                    "$type $token",
                    "accepted",
                    data.id
                )
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

    private fun actionReject(data: SlotsByReqIdItem, position: Int) {
        GlobalScope.launch {
            try {
                val res = ApiAdapter.apiClient.slotAction(
                    "$type $token",
                    "rejected",
                    data.id
                )
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
    private fun showAlert(data: SlotsByReqIdItem, position: Int) {
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
}