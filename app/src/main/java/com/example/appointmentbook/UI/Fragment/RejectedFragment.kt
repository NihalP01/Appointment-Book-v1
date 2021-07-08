package com.example.appointmentbook.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.UI.Adapter.AdminPanelRejectedAdapter
import com.example.appointmentbook.data.sample.SlotPendingDataItem
import com.example.appointmentbook.utils.Utils
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        recyclerRejected.apply {
            adapter = adminPanelRejectedAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = ApiAdapter.apiClient.slotReqRejected("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    adminPanelRejectedAdapter.list =
                        response.body() as ArrayList<SlotPendingDataItem>
                    adminPanelRejectedAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    companion object {
        fun create(): RejectedFragment {
            return RejectedFragment()
        }
    }
}