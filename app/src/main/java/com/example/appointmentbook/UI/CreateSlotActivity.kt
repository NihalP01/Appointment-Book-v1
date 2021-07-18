package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_add_slot.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateSlotActivity : AppCompatActivity() {

    private val type = getAuthType(AUTH_TYPE)
    private val token = getToken(TOKEN_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slot)
        supportActionBar?.hide()

        slotStartTime.setOnClickListener {
            //
            DateTimePicker().start(
                fragmentManager = supportFragmentManager,
                title = "Select slot start"
            ) { cancelled, dateTime ->
                //
                if (cancelled) {
                    //show cancelled
                    return@start
                }
                // dateTime is the selected one
                Log.d("mridx", "selected one is: $dateTime")
            }
        }

        slotEndTime.setOnClickListener {
            //
        }

        bookingStartTime.setOnClickListener {
            //
        }

        bookingEndTime.setOnClickListener {
            //
        }

        val slotCapacity = slotCapacity.text

        btnCreateSlot.setOnClickListener {
            showAlert()
        }
    }

    private fun createMySlot() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response =
                    ApiAdapter.apiClient.createSlots(
                        "$type $token",
                        "",
                        "",
                        "",
                        "",
                        0,
                        true
                    )
                if (response.isSuccessful && response.body() != null) {
                    toast("Slot created Successfully")
                    startActivity(Intent(this@CreateSlotActivity, DoctorSlots::class.java))
                    finish()
                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }


    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you Sure ?")
            .setIcon(R.drawable.ic_warning)
            .setMessage("By confirming this, A new slot will be created")
            .setPositiveButton("Confirm") { dialog, which ->
                createSlot()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .show()
    }

    private fun createSlot() {
        GlobalScope.launch(Dispatchers.Main) {
            try {

            } catch (e: Exception) {

            }
        }
    }


}