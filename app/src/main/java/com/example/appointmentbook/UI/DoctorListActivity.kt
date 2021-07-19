package com.example.appointmentbook.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.DoctorListAdapter
import com.example.appointmentbook.UI.Login.User.UserLoginActivity
import com.example.appointmentbook.data.DoctorListData.DoctorsListData
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.DOC_ID
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.USER_NAME
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getPreference
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.getUserName
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.example.appointmentbook.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_doctor_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class DoctorListActivity : AppCompatActivity() {

    private val userName = getUserName(USER_NAME)
    private val userToken = getToken(TOKEN_KEY)
    val type = getAuthType(AUTH_TYPE)


    private val doctorPanelAdapter by lazy {
        DoctorListAdapter().apply {
            btnViewSlot = viewSlot
        }
    }

    private val viewSlot = { position: Int, data: DoctorsListData ->
        slotList(data.id.toString())
//        startActivity(Intent(this, SlotsActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        supportActionBar?.hide()
        userName1.text = userName

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.docListRefresh)

        swipeRefreshLayout?.post ( Runnable{
            run{
                swipeRefreshLayout.isRefreshing = true
                getDoctorList(userToken, type)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            getDoctorList(userToken, type)
        }

        btnUserLogout1.setOnClickListener {
            showAlert()
        }

        materialTextView20.setOnClickListener {
            startActivity(Intent(this, SlotsActivity::class.java))
        }



        doctorRecycler.apply {
            layoutManager = LinearLayoutManager(this@DoctorListActivity)
            adapter = doctorPanelAdapter
            setHasFixedSize(true)

        }
    }

    private fun slotList(docId: String) {
        val sharedPreferences = getPreference()
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        edit.putString(DOC_ID, docId)
        edit.apply()
        Log.d("nihal", docId)
        startActivity(Intent(this, SlotsActivity::class.java))
    }

    private fun getDoctorList(token: String, type: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                docListRefresh.isRefreshing = true
                val response = ApiAdapter.apiClient.doctorList("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    docListRefresh.isRefreshing = false
                    doctorPanelAdapter.list = response.body() as ArrayList<DoctorsListData>
                    doctorPanelAdapter.notifyDataSetChanged()
                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    private fun doUserLogout() {
        logout()
        startActivity(Intent(this, UserLoginActivity::class.java))
        finish()
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Warning !")
            .setIcon(R.drawable.ic_warning)
            .setMessage("Do you want to logout?")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                doUserLogout()
            }
            .show()
    }

}


