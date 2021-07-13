package com.example.appointmentbook.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.AdminPanelAcceptedAdapter
import com.example.appointmentbook.data.sample.SlotPendingDataItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import kotlinx.android.synthetic.main.fragment_accepted.*
import kotlinx.android.synthetic.main.fragment_pending.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AcceptedFragment : Fragment() {

    private val adminPanelAcceptedAdapter by lazy {
        AdminPanelAcceptedAdapter().apply {
            //reserved for future work
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accepted, container, false)
        val recyclerPending: RecyclerView = view.findViewById(R.id.recycler_pending)
        val progressBar: ProgressBar = view.findViewById(R.id.progressbarAccepted)
//        progressBar.visibility = View.VISIBLE
        val type = requireContext().getAuthType(AUTH_TYPE)
        val token = requireContext().getToken(TOKEN_KEY)
        val acceptedSwipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.acceptedRefresh)

        recyclerPending.apply {
            adapter = adminPanelAcceptedAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        acceptedSwipeRefresh?.post(kotlinx.coroutines.Runnable {
            kotlin.run {
                acceptedSwipeRefresh.isRefreshing = true
                loadRecycler(type, token)
            }
        })

        acceptedSwipeRefresh?.setOnRefreshListener {
            loadRecycler(type, token)
        }

        return view
    }

    private fun loadRecycler(type: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                acceptedRefresh.isRefreshing = true
                val response = ApiAdapter.apiClient.slotReqAccepted("$type $token")
                if (response.isSuccessful && response.body() != null) {
//                    progressBar.visibility = View.INVISIBLE
                    acceptedRefresh.isRefreshing = false
                    adminPanelAcceptedAdapter.list.sortBy { it.slot_data.slot.name }
                    adminPanelAcceptedAdapter.list =
                        response.body() as ArrayList<SlotPendingDataItem>

                    adminPanelAcceptedAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun create(): AcceptedFragment {
            return AcceptedFragment()
        }
    }
}