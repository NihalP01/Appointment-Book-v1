package com.example.appointmentbook.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Login.DOctor.DoctorLoginActivity
import com.example.appointmentbook.data.SlotsData
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.DOC_ID
import com.example.appointmentbook.utils.Utils.Companion.SLOT_ID
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.docId
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.example.appointmentbook.utils.Utils.Companion.toast
import kotlinx.android.synthetic.main.activity_doc_slots.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class DoctorSlots : AppCompatActivity() {

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

        btnFloatingAddSlot.setOnClickListener {
            startActivity(Intent(this, CreateSlotActivity::class.java))
        }

        btnAdminLogout.setOnClickListener {
            logout()
            startActivity(Intent(this, DoctorLoginActivity::class.java))
            finish()
        }

        notificationRecycler.apply {
            adapter = doctorSlot
            layoutManager = LinearLayoutManager(this@DoctorSlots)
            setHasFixedSize(true)
        }

        fetchData()
    }

    private fun fetchData() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val type = getAuthType(AUTH_TYPE)
                val token = getToken(TOKEN_KEY)
                val docId = docId(DOC_ID)
                val response = ApiAdapter.apiClient.docSlotAvailable("$type $token", docId.toInt())
                if (response.isSuccessful && response.body() != null) {
                    doctorSlot.list = response.body() as ArrayList<SlotsData>
                    doctorSlot.notifyDataSetChanged()
                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.toString())
            }
        }
    }
}