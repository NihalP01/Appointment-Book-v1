package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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

    lateinit var _slotStartTime: String
    lateinit var _slotEndTime: String
    lateinit var _bookingStartTime: String
    lateinit var _bookingEndTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slot)
        supportActionBar?.hide()

        btnBack1.setOnClickListener {
            finish()
        }

        slotStartTime.hint = "Slot start time"
        slotStartTime.setOnClickListener {
            DateTimePicker().start(
                fragmentManager = supportFragmentManager,
                title = "Select slot start"
            ) { cancelled, dateTime ->
                if (cancelled) {
                    return@start
                }
                _slotStartTime = dateTime.toString()
                slotStartTime.hint = dateTime.toString()
                Log.d("myTag", _slotStartTime)
            }
        }

        slotEndTime.hint = "Slot End time"
        slotEndTime.setOnClickListener {
            DateTimePicker().start(
                fragmentManager = supportFragmentManager,
                title = "Select slot end time"
            ) { cancelled, dateTime ->
                if (cancelled) {
                    return@start
                }
                _slotEndTime = dateTime.toString()
                slotEndTime.hint = dateTime.toString()
            }
        }

        bookingStartTime.hint = "Booking start time"
        bookingStartTime.setOnClickListener {
            DateTimePicker().start(
                fragmentManager = supportFragmentManager,
                title = "Select booking start time"
            ) { cancelled, dateTime ->
                if (cancelled) {
                    return@start
                }
                _bookingStartTime = dateTime.toString()
                bookingStartTime.hint = dateTime.toString()
            }
        }

        bookingEndTime.hint = "Booking end time"
        bookingEndTime.setOnClickListener {
            DateTimePicker().start(
                fragmentManager = supportFragmentManager,
                title = "Select booking ending time"
            ) { cancelled, dateTime ->
                if (cancelled) {
                    return@start
                }
                _bookingEndTime = dateTime.toString()
                bookingEndTime.hint = dateTime.toString()
            }
        }

        btnCreateSlot.setOnClickListener {
            showAlert()
        }

    }

    private fun createSlot() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                createProgressBar.visibility = View.VISIBLE
                btnCreateSlot.visibility = View.INVISIBLE
                val response =
                    ApiAdapter.apiClient.createSlots(
                        "$type $token",
                        _slotStartTime,
                        _slotEndTime,
                        _bookingStartTime,
                        _bookingEndTime,
                        slotCapacity.text.toString().toInt(),
                        true
                    )
                if (response.isSuccessful && response.body() != null) {
                    createProgressBar.visibility = View.INVISIBLE
                    btnCreateSlot.visibility = View.VISIBLE
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

}