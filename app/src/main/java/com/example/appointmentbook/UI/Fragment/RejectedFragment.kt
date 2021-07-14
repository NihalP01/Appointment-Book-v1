package com.example.appointmentbook.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.AdminPanelRejectedAdapter
import com.example.appointmentbook.data.SlotBookRequests.SlotBookRequestsItem
import com.example.appointmentbook.utils.Utils
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import kotlinx.android.synthetic.main.fragment_rejected.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class RejectedFragment : Fragment() {

    private val adminPanelRejectedAdapter by lazy {
        AdminPanelRejectedAdapter().apply {
            //reserved for future work
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rejected, container, false)
        val type = requireContext().getAuthType(Utils.AUTH_TYPE)
        val token = requireContext().getToken(Utils.TOKEN_KEY)
        val recyclerRejected: RecyclerView = view.findViewById(R.id.recycler_rejected)
//        val progressBar: ProgressBar = view.findViewById(R.id.progressbarRejected)
//        progressBar.visibility = View.VISIBLE
        val rejectedSwipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.rejectedRefresh)
        recyclerRejected.apply {
            adapter = adminPanelRejectedAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        rejectedSwipeRefresh.post(Runnable {
            kotlin.run {
                rejectedSwipeRefresh.isRefreshing = true
                loadRecycler(type, token)
            }
        })

        rejectedSwipeRefresh.setOnRefreshListener {
            loadRecycler(type, token)
        }

        return view
    }

    private fun loadRecycler(type: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                rejectedRefresh.isRefreshing = true
                val response = ApiAdapter.apiClient.slotReqRejected("$type $token")
                if (response.isSuccessful && response.body() != null) {
//                    progressBar.visibility = View.INVISIBLE
                    rejectedRefresh.isRefreshing = false
                    adminPanelRejectedAdapter.list =
                        response.body() as ArrayList<SlotBookRequestsItem>
                    adminPanelRejectedAdapter.notifyDataSetChanged()
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
        fun create(): RejectedFragment {
            return RejectedFragment()
        }
    }
}