package com.example.medomind.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.medomind.Network.ApiAdapter
import com.example.medomind.R
import com.example.medomind.data.SlotsbyReqIdData.SlotsByReqIdItem
import com.example.medomind.utils.Utils.Companion.AUTH_TYPE
import com.example.medomind.utils.Utils.Companion.SLOT_ID
import com.example.medomind.utils.Utils.Companion.TOKEN_KEY
import com.example.medomind.utils.Utils.Companion.getAuthType
import com.example.medomind.utils.Utils.Companion.getSlotId
import com.example.medomind.utils.Utils.Companion.getToken
import com.example.medomind.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_slot_req_by_id.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DoctorSlotReqActivity : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    private val token = getToken(TOKEN_KEY)
    private val slotId = getSlotId(SLOT_ID)

    private val doctorPanelAdapter by lazy {
        DoctorSlotReqAdapter().apply {
            btnAcceptDoc = btnAcceptClick
            btnRejectDoc = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: SlotsByReqIdItem ->
        actionAccept(data)
    }

    private val btnRejectClick = { position: Int, data: SlotsByReqIdItem ->
        showAlert(data)
    }

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slot_req_by_id)
        supportActionBar?.hide()

        btnBack3.setOnClickListener {
            finish()
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.reqSwipeRefresh)

        swipeRefreshLayout?.post(kotlinx.coroutines.Runnable {
            kotlin.run {
                swipeRefreshLayout.isRefreshing = true
                getData()
            }
        })

        swipeRefreshLayout?.setOnRefreshListener {
            getData()
        }

        recSlotReqById.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = doctorPanelAdapter
            setHasFixedSize(true)
        }

    }


    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                reqSwipeRefresh.isRefreshing = true
                val response = ApiAdapter.apiClient.slotsByID("$type $token", slotId.toInt())
                if (response.isSuccessful && response.body() != null) {
                    reqSwipeRefresh.isRefreshing = false
                    if (response.body()!!.isEmpty()) {
                        notiNoReq.visibility = View.VISIBLE
                    } else {
                        doctorPanelAdapter.list = response.body() as ArrayList<SlotsByReqIdItem>
                        doctorPanelAdapter.notifyDataSetChanged()
                    }
                } else {
                    toast(response.body().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    private fun actionAccept(data: SlotsByReqIdItem) {
        GlobalScope.launch {
            try {
                reqSwipeRefresh.isRefreshing = true
                val res = ApiAdapter.apiClient.slotAction(
                    "$type $token",
                    "accept",
                    data.id
                )
                if (res.isSuccessful && res.body() != null) {
                    reqSwipeRefresh.isRefreshing = false
                    toast("Request has been accepted")
                } else {
                    toast(res.message().toString())
                }
            } catch (e: Exception) {
                Log.d("myTag", e.message.toString())
            }
        }
    }

    private fun actionReject(data: SlotsByReqIdItem) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                reqSwipeRefresh.isRefreshing = true
                val res = ApiAdapter.apiClient.slotAction(
                    "$type $token",
                    "reject",
                    data.id
                )
                if (res.isSuccessful && res.body() != null) {
                    reqSwipeRefresh.isRefreshing = false
                    toast("Request has been rejected")
                } else {
                    toast(res.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    //show a confirmation alert dialog
    private fun showAlert(data: SlotsByReqIdItem) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Are you sure to reject ?")
            .setMessage("On confirming, the Book request of with slot number  will be rejected. will receive a notification of the same")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                actionReject(data)
            }
            .show()
    }
}