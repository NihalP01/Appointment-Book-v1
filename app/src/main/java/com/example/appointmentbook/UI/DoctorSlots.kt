package com.example.appointmentbook.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appointmentbook.MainActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.DOctor.DoctorLoginActivity
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.DOC_ID
import com.example.appointmentbook.utils.Utils.Companion.SLOT_ID
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.docId
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.getUserName
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.example.appointmentbook.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_doc_slots.*
import kotlinx.android.synthetic.main.fragment_pending.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class DoctorSlots : AppCompatActivity() {

    val docName = getUserName(USER_NAME)

    private val doctorSlot by lazy {
        DoctorSlotsAdapter().apply {
            btnSlot = btnViewReq
        }
    }

    private val btnViewReq = { position: Int, data: SlotsData ->
        val sharedPreferences = getPreference()
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putString(SLOT_ID, data.id.toString())
        edit.apply()
        startActivity(Intent(this, DoctorSlotReqActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doc_slots)
        supportActionBar?.hide()
        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.docSlotRefresh)

        adminName.text = docName

        btnUpDateInfo.setOnClickListener {
            startActivity(Intent(this, DocInfoUpdate::class.java))
        }

        swipeRefreshLayout?.post(kotlinx.coroutines.Runnable {
            kotlin.run {
                swipeRefreshLayout.isRefreshing = true
                fetchData()
            }
        })

        swipeRefreshLayout?.setOnRefreshListener {
            fetchData()
        }

        btnFloatingAddSlot.setOnClickListener {
            startActivity(Intent(this, CreateSlotActivity::class.java))
        }

        btnAdminLogout.setOnClickListener {
            showAlert()
        }

        notificationRecycler.apply {
            adapter = doctorSlot
            layoutManager = LinearLayoutManager(this@DoctorSlots)
            setHasFixedSize(true)
        }
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                docSlotRefresh.isRefreshing = true
                val type = getAuthType(AUTH_TYPE)
                val token = getToken(TOKEN_KEY)
                val docId = docId(DOC_ID)
                Log.d("docId", docId)
                val response = ApiAdapter.apiClient.docSlotAvailable("$type $token", docId.toInt())
                if (response.isSuccessful && response.body() != null) {
                    if (response.body()!!.isEmpty()) {
                        notiEmptySlot.visibility = View.VISIBLE
                        docSlotRefresh.isRefreshing = false
                    } else {
                        docSlotRefresh.isRefreshing = false
                        notiEmptySlot.visibility = View.INVISIBLE
                        doctorSlot.list = response.body() as ArrayList<SlotsData>
                        doctorSlot.notifyDataSetChanged()
                    }

                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.toString())
            }
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning !")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Do you want to logout ?")
            .setPositiveButton("Confirm") { dialog, which ->
                logout()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .show()
    }
}