package com.example.medomind.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medomind.Network.ApiAdapter
import com.example.medomind.R
import com.example.medomind.UI.Adapter.userBookReqAdapter
import com.example.medomind.data.AlllBookReq.AllBookReqDataItem
import com.example.medomind.utils.Utils.Companion.AUTH_TYPE
import com.example.medomind.utils.Utils.Companion.TOKEN_KEY
import com.example.medomind.utils.Utils.Companion.getAuthType
import com.example.medomind.utils.Utils.Companion.getToken
import com.example.medomind.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_book_req.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookReqActivity : AppCompatActivity() {
    private val bookReqAdapter by lazy {
        userBookReqAdapter().apply {
            btnContact = btnContactDoc
        }
    }

    private val btnContactDoc = { position: Int, data: AllBookReqDataItem ->
        showAlert(data.slot.associated_to.phone)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_req)
        supportActionBar?.hide()
        myReqProgress.visibility = View.VISIBLE

        btnBack4.setOnClickListener {
            finish()
        }

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
                    if (response.body()!!.isEmpty()) {
                        emptyBodyMsg2.visibility = View.VISIBLE
                        myReqProgress.visibility = View.INVISIBLE
                    } else {
                        myReqProgress.visibility = View.INVISIBLE
                        emptyBodyMsg2.visibility = View.INVISIBLE
                        bookReqAdapter.list = response.body() as ArrayList<AllBookReqDataItem>
                        bookReqAdapter.notifyDataSetChanged()
                    }

                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    //to open whatsapp chat
    private fun actionContact(phone: String) {
        val phoneNo = "+91${phone}"
        Log.d("docPh", phone)
        val url = "https://api.whatsapp.com/send?phone=$phoneNo"
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }

    private fun showAlert(phoneNo: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning")
            .setIcon(R.drawable.ic_warning)
            .setMessage("You are requested to contact only via WhatsApp. Please do not make phone call directly without having permission from the Doctor.")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                actionContact(phoneNo)
            }
            .show()
    }
}