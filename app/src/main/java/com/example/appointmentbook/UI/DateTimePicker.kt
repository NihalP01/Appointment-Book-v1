package com.example.appointmentbook.UI

import android.util.Log
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimePicker {

    private var _onDone: ((cancelled: Boolean, dateTime: String?) -> Unit)? = null

    private var date = ""


    @Deprecated(
        message = "use other one",
        replaceWith = ReplaceWith(expression = "start(fragmentManager: FragmentManager, title: String?, onDone: ((cancelled: Boolean, dateTime: String?) -> Unit)"),
        level = DeprecationLevel.ERROR
    )
    fun start(
        fragmentManager: FragmentManager,
        onDone: ((cancelled: Boolean, dateTime: String?) -> Unit)
    ) {
        start(fragmentManager, null, onDone)
    }

    fun start(
        fragmentManager: FragmentManager,
        title: String?,
        onDone: ((cancelled: Boolean, dateTime: String?) -> Unit)
    ) {
        this._onDone = onDone
        datePick(title = title, fragmentManager = fragmentManager)
    }

    private val sdf by lazy {
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    private fun formatTimeStampToDate(s: Long): String {
        return sdf.format(s)
    }

    private fun datePick(title: String?, fragmentManager: FragmentManager) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("$title date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            it?.let { s ->
                date = formatTimeStampToDate(s)
                timePick(title = title, fragmentManager = fragmentManager)
                datePicker.dismiss() //dismiss
            }
        }
        datePicker.addOnNegativeButtonClickListener {
            //
            _onDone?.invoke(true, "")
            datePicker.dismiss() //dismiss
        }
        datePicker.show(fragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun timePick(title: String?, fragmentManager: FragmentManager) {
        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setTitleText("$title time")
                .build()
        timePicker.addOnPositiveButtonClickListener {
            _onDone?.invoke(false, "$date ${timePicker.hour}:${timePicker.minute}")
            timePicker.dismiss()
        }
        timePicker.addOnNegativeButtonClickListener {
            _onDone?.invoke(true, "")
            timePicker.dismiss() //dismiss
        }
        timePicker.show(fragmentManager, "MATERIAL_TIME_PICKER")
    }

}
