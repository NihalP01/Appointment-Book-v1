package com.example.appointmentbook.UI

import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat

class DateTimePicker{

    var onDone: ((cancelled: Boolean, dateTime: String?)-> Unit)? = null
        set(value) = run { field = value }


    private fun start(){
        datePick()
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
        //datePicker.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun timePick(date: String) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setTitleText("Select appointment time")
                .build()
        timePicker.addOnPositiveButtonClickListener {
            onDone?.invoke(false, "$date ${timePicker.hour}:${timePicker.minute}")
        }
        timePicker.addOnNegativeButtonClickListener {
            onDone?.invoke(true, "")
        }
       // timePicker.show(supportFragmentManager, "MATERIAL_TIME_PICKER")
    }

}
