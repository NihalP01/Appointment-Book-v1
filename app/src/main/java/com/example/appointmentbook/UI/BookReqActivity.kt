package com.example.appointmentbook.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.BookReqAdapter
import com.example.appointmentbook.data.AlllBookReq.AllBookReqDataItem
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
        BookReqAdapter().apply {
            btnContact = btnContactDoc
        }
    }

    private val btnContactDoc = { position: Int, data: AllBookReqDataItem ->
        showAlert()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_req)
        supportActionBar?.hide()

        myReqProgress.visibility = View.VISIBLE

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
                    if (response.body()!!.isEmpty()){
                        emptyBodyMsg2.visibility = View.VISIBLE
                        myReqProgress.visibility = View.INVISIBLE
                    }else{
                        myReqProgress.visibility = View.INVISIBLE
                        emptyBodyMsg2.visibility = View.INVISIBLE
                        bookReqAdapter.list = response.body() as ArrayList<AllBookReqDataItem>
                        bookReqAdapter.notifyDataSetChanged()
                    }

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
    private fun actionContact() {
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