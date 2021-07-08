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
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.AdminPanelAdapter
import com.example.appointmentbook.data.sample.SlotPendingDataItem
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PendingReqFragment : Fragment() {

    private val adminPanelAdapter by lazy {
        AdminPanelAdapter().apply {
            btnAcceptAdmin = btnAcceptClick
            btnRejectAdmin = btnRejectClick
        }
    }

    private val btnAcceptClick = { position: Int, data: SlotPendingDataItem ->
        actionAccept(data)
    }

    private val btnRejectClick = { position: Int, data: SlotPendingDataItem ->
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

        recyclerAllReq.apply {
            adapter = adminPanelAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        Log.d("myTag", token.toString())
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.slotReqPending("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    Log.d("myTag", response.body().toString())
                    adminPanelAdapter.list = response.body() as ArrayList<SlotPendingDataItem>
                    adminPanelAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        container?.addView(view)
        return view
    }

    companion object {
        fun create(): PendingReqFragment {
            return PendingReqFragment()
        }
    }

    private fun actionAccept(data: SlotPendingDataItem) {
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

    private fun actionReject(data: SlotPendingDataItem) {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Are you sure to reject ?")
            .setMessage("On confirming, the Book request of ${data.requested_by.name} with slot number ${data.slot_data.slot_id} will be rejected. ${data.requested_by.name} will receive a notification of the same")
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
