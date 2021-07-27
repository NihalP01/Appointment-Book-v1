package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.SlotsAdapter
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.DOC_ID
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.docId
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_slots.*
import kotlinx.android.synthetic.main.slots_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class SlotsActivity : AppCompatActivity() {

    private val docID: String = docId(DOC_ID)

    private val slotsAdapter by lazy {
        SlotsAdapter().apply {
            btnBookSlot = itemClicked
        }
    }

    private val itemClicked = { position: Int, data: SlotsData ->
        bookSlot(data.id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slots)
        supportActionBar?.hide()

        userDetails.setOnClickListener {
            startActivity(Intent(this, UserDetails::class.java))
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.bookSlotRefresh)

        swipeRefreshLayout.post(Runnable {
            kotlin.run {
                swipeRefreshLayout.isRefreshing = true
                getSlots()
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            getSlots()
        }

        slotsRecycler.visibility = View.INVISIBLE

        slotsRecycler.apply {
            adapter = slotsAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SlotsActivity)
        }

    }

    private fun getSlots() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                bookSlotRefresh.isRefreshing = true
                val token = getToken(TOKEN_KEY)
                val type = getAuthType(AUTH_TYPE)
                val response =
                    ApiAdapter.apiClient.docSlotAvailable("$type $token", docID.toInt())
                if (response.isSuccessful && response.body() != null) {
                    bookSlotRefresh.isRefreshing = false
                    slotsRecycler.visibility = View.VISIBLE
                    if (response.body()!!.isEmpty()) {
                        emptyBodyMsg.visibility = View.VISIBLE
                    } else {
                        emptyBodyMsg.visibility = View.INVISIBLE
                        slotsAdapter.list = response.body() as ArrayList<SlotsData>
                        slotsAdapter.notifyDataSetChanged() //this line can be invoked from about code, inside adapter class
                    }
                } else {
                    Toast.makeText(
                        this@SlotsActivity,
                        response.message().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@SlotsActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun bookSlot(id: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                bookProgress.visibility = View.VISIBLE
                btnBookSlot.visibility = View.INVISIBLE
                val type = getAuthType(AUTH_TYPE)
                val token = getToken(TOKEN_KEY)
                val response = ApiAdapter.apiClient.bookSlot(
                    "$type $token",
                    id
                )
                if (response.isSuccessful && response.body() != null) {
                    bookProgress.visibility = View.INVISIBLE
                    btnBookSlot.visibility = View.VISIBLE
                    if (response.body()!!.message.length > 1){
                        toast(response.body()!!.message)
                        startActivity(Intent(this@SlotsActivity, BookReqActivity::class.java))
                    }else{
                        toast("Booking request placed")
                        startActivity(Intent(this@SlotsActivity, BookReqActivity::class.java))
                    }
                }else{
                    bookProgress.visibility = View.INVISIBLE
                    btnBookSlot.visibility = View.VISIBLE
                    toast("Something went wrong ! Try after sometime.")
                }
            } catch (ex: Exception) {
                //
            }
        }
    }


//    private fun informUser(message: String) {
//        runOnUiThread {
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//            //let's show user's requests
//            startActivity(Intent(this, BookReqActivity::class.java))
//        }
//    }
}