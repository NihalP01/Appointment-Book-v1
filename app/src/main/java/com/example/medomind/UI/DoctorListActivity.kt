package com.example.medomind.UI

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.medomind.Network.ApiAdapter
import com.example.medomind.R
import com.example.medomind.UI.Adapter.DoctorListAdapter
import com.example.medomind.data.DoctorListData.DoctorsListData
import com.example.medomind.utils.Utils.Companion.AUTH_TYPE
import com.example.medomind.utils.Utils.Companion.DOC_ID
import com.example.medomind.utils.Utils.Companion.TOKEN_KEY
import com.example.medomind.utils.Utils.Companion.USER_NAME
import com.example.medomind.utils.Utils.Companion.getAuthType
import com.example.medomind.utils.Utils.Companion.getPreference
import com.example.medomind.utils.Utils.Companion.getToken
import com.example.medomind.utils.Utils.Companion.getUserName
import com.example.medomind.utils.Utils.Companion.toast
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

        username1.setOnClickListener {
            startActivity(Intent(this, BookReqActivity::class.java))
        }

        val swipeRefreshLayout = findViewById<SwipeRefreshLayout>(R.id.docListRefresh)

        swipeRefreshLayout?.post(Runnable {
            run {
                swipeRefreshLayout.isRefreshing = true
                getDoctorList(userToken, type)
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            getDoctorList(userToken, type)
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
                    toast("Something went wrong, try again later.")
                    Log.d("myTag", response.message())
                }
            } catch (e: Exception) {
                toast("Something went wrong, try again later.")
                Log.d("myTag", e.message.toString())
            }
        }
    }
}


