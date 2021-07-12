package com.example.appointmentbook.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.BookReqAdapter2
import com.example.appointmentbook.data.BookRequestData.BookRequestDataItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_book_req.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookReqActivity : AppCompatActivity() {
    private val bookReqAdapter by lazy {
        BookReqAdapter2().apply {
            btnContact = btnContactDoc
        }
    }

    private val btnContactDoc = { position: Int, data: BookRequestDataItem ->
        showAlert()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_req)
        supportActionBar?.hide()

        rvBookReq.apply {
            layoutManager = LinearLayoutManager(this@BookReqActivity)
            adapter = bookReqAdapter
            setHasFixedSize(true)
        }

        GlobalScope.launch(Dispatchers.Main) {
            val type = getAuthType(AUTH_TYPE)
            val token = getToken(TOKEN_KEY)
            val response = ApiAdapter.apiClient.bookReq("$type $token")
            try {
                if (response.isSuccessful && response.body() != null) {
                    bookReqAdapter.list = response.body() as ArrayList<BookRequestDataItem>
                    bookReqAdapter.list.sortBy { it.slot_data.slot.name }
                    bookReqAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        this@BookReqActivity,
                        response.message().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@BookReqActivity, e.message.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //to open whatsapp chat
    private fun actionContact(){
        val phoneNo = "+917002028029"
        val url = "https://api.whatsapp.com/send?phone=$phoneNo"
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setIcon(R.drawable.ic_warning)
            .setMessage("You are requested to contact only via WhatsApp. Please do not make phone call directly without having permission from the Doctor.")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                actionContact()
                Toast.makeText(this, "HAHAAHAHA", Toast.LENGTH_SHORT).show()
            }
            .show()
    }
}