package com.example.appointmentbook.UI.Fragment

import android.os.Bundle
import android.util.Log
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
import com.example.appointmentbook.UI.AdminPanelAdapter
import com.example.appointmentbook.data.SlotBookRequests.SlotBookRequestsItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_pending.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

class PendingReqFragment : Fragment() {

    private val adminPanelAdapter by lazy {
        AdminPanelAdapter().apply {
            btnAcceptAdmin = btnAcceptClick
            btnRejectAdmin = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: SlotBookRequestsItem ->
        actionAccept(data)
    }

    private val btnRejectClick = { position: Int, data: SlotBookRequestsItem ->
        actionReject(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View? = inflater.inflate(R.layout.fragment_pending, container, false)
        val recyclerAllReq: RecyclerView = view!!.findViewById(R.id.recyclerAllReq)
        val type = requireContext().getAuthType(AUTH_TYPE)
        val token = requireContext().getToken(TOKEN_KEY)
        val swipeRefresh = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        recyclerAllReq.apply {
            adapter = adminPanelAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        swipeRefresh.post(Runnable {
            kotlin.run {
                swipeRefresh.isRefreshing = true
                loadRecyclerData(type, token)
            }
        })


        swipeRefresh.setOnRefreshListener {
            // Fetching data from server
            loadRecyclerData(type, token)
        }

        container?.addView(view)
        return view
    }


    companion object {
        fun create(): PendingReqFragment {
            return PendingReqFragment()
        }
    }


    private fun loadRecyclerData(type: String, token: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                swipeRefresh.isRefreshing = true
                val response = ApiAdapter.apiClient.slotReqPending("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    swipeRefresh.isRefreshing = false
                    if (response.body()!!.isEmpty()) {
                        pendingReqMessage.visibility = View.VISIBLE
                        recyclerAllReq.visibility = View.INVISIBLE
                    } else {
                        pendingReqMessage.visibility = View.INVISIBLE
                        adminPanelAdapter.list = response.body() as ArrayList<SlotBookRequestsItem>
                        adminPanelAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actionAccept(data: SlotBookRequestsItem) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val type = requireContext().getAuthType(AUTH_TYPE)
                val token = requireContext().getToken(TOKEN_KEY)

                val res = ApiAdapter.apiClient.slotAction("$type $token", "accepted", data.id)
                Log.d("myTag", res.body().toString())
                if (res.isSuccessful && res.body() != null) {
                    Toast.makeText(
                        context,
                        "Request has been accepted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d("myTag", res.message().toString())
                }
            } catch (e: Exception) {
                Log.d("myTag", e.message.toString())
            }
        }
    }

    private fun actionReject(data: SlotBookRequestsItem) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure to reject ?")
            .setMessage("On confirming, the Book request of  with slot number  will be rejected.  will receive a notification of the same")
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .setPositiveButton("Confirm") { dialog, which ->
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val type = requireContext().getAuthType(AUTH_TYPE)
                        val token = requireContext().getToken(TOKEN_KEY)
                        val res =
                            ApiAdapter.apiClient.slotAction("$type $token", "rejected", data.id)
                        if (res.isSuccessful && res.body() != null) {
                            Toast.makeText(
                                context,
                                "Request has been rejected",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.d("myTag", res.message().toString())
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
            .show()
    }
}
