package com.example.appointmentbook.UI.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appointmentbook.R

class RejectedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rejected, container, false)
    }

    companion object {
        fun create(): RejectedFragment {
            return RejectedFragment()
        }
    }
}