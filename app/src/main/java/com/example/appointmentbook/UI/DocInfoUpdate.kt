package com.example.appointmentbook.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.R
import com.example.appointmentbook.utils.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_update_doc_info.*

class DocInfoUpdate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_update_doc_info)
        btnUpdate?.setOnClickListener {
            toast("This is not available right now")
        }
    }
}