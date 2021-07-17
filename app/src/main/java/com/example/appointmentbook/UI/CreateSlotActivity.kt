package com.example.appointmentbook.UI

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.R
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.android.synthetic.main.activity_add_slot.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CreateSlotActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_slot)
        supportActionBar?.hide()


        slotStartTime.setOnClickListener {
            datePick()
        }

        slotEndTime.setOnClickListener {
            datePick()
        }

        bookingStartTime.setOnClickListener {
            datePick()
        }

        bookingEndTime.setOnClickListener {
            datePick()
        }

        val slotCapacity = slotCapacity.text

        btnCreateSlot.setOnClickListener {
            showAlert()
        }
    }

    private fun datePick() {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select appointment date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            val getDate = datePicker.headerText
            timePick(getDate)
        }
        datePicker.addOnNegativeButtonClickListener {
            //
        }
        datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")

    }

    private fun timePick(date: String) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setTitleText("Select appointment time")
                .build()
        timePicker.addOnPositiveButtonClickListener {
            Log.d("finalTime", "$date ${timePicker.hour}:${timePicker.minute}")
            showToast("Picked Time: $date ${timePicker.hour}:${timePicker.minute}")
        }
        timePicker.addOnNegativeButtonClickListener {
            //
        }

        timePicker.show(supportFragmentManager, "MATERIAL_TIME_PICKER")
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

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}