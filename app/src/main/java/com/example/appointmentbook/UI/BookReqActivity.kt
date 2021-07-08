package com.example.appointmentbook.UI

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.BookReqAdapter
import com.example.appointmentbook.data.BookRequestData.BookRequestDataItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import kotlinx.android.synthetic.main.activity_book_req.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class BookReqActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_req)
        supportActionBar?.hide()

        GlobalScope.launch(Dispatchers.Main) {
            val type = getAuthType(AUTH_TYPE)
            val token = getToken(TOKEN_KEY)
            val response = ApiAdapter.apiClient.bookReq("$type $token")
            try {
                if (response.isSuccessful && response.body() != null) {
                    rvBookReq.apply {
                        layoutManager = LinearLayoutManager(this@BookReqActivity)
                        adapter = BookReqAdapter(response.body() as ArrayList<BookRequestDataItem>)
                        setHasFixedSize(true)
                    }
                } else {

                }
            } catch (e: Exception) {

            }

        }
    }
}