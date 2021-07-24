package com.example.appointmentbook.UI

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.appointmentbook.MainActivity
import com.example.appointmentbook.Network.ApiAdapter
import com.example.appointmentbook.R
import com.example.appointmentbook.data.Details
import com.example.appointmentbook.data.DetailsUpdateData
import com.example.appointmentbook.utils.Utils.Companion.AUTH_TYPE
import com.example.appointmentbook.utils.Utils.Companion.TOKEN_KEY
import com.example.appointmentbook.utils.Utils.Companion.getAuthType
import com.example.appointmentbook.utils.Utils.Companion.getToken
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.example.appointmentbook.utils.Utils.Companion.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_update_doc_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DocInfoUpdate : AppCompatActivity() {

    val type = getAuthType(AUTH_TYPE)
    val token = getToken(TOKEN_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(R.layout.activity_update_doc_info)

        currentInfo()

        btnAdminLogout.setOnClickListener {
            showAlert()
        }

        btnUpdate?.setOnClickListener {
            validateInput()
        }
    }

    private fun validateInput() {
        if (docWorksAt.text.toString().isEmpty()) {
            docWorksAt.error = "Enter where you work"
            docWorksAt.requestFocus()
            return
        }

        if (docSpeciality.text.toString().isEmpty()) {
            docSpeciality.error = "Enter your speciality"
            docSpeciality.requestFocus()
            return
        }
        doUpdate()
    }

    private fun currentInfo() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                docDetailsCardView.visibility = View.INVISIBLE
                docDetailsProgressBar.visibility = View.VISIBLE
                val response = ApiAdapter.apiClient.role("$type $token")
                if (response.isSuccessful && response.body() != null) {
                    docDetailsProgressBar.visibility = View.INVISIBLE
                    docDetailsCardView.visibility = View.VISIBLE
                    docCurrentInfo.text =
                        "Name: ${response.body()!!.data.user.name}\nEmail: ${response.body()!!.data.user.email}\nPhone: ${response.body()!!.data.user.phone}"
                    if (response.body()!!.data.details.details == null) {
                        val txt = "Not available, please update your work details"
                        docWorkDetails.text = txt
                    } else {
                        val WorksAt = response.body()!!.data.details.details.works_at
                        val speciality = response.body()!!.data.details.details.speciality
                        docWorkDetails.text = "Work Details: $WorksAt\nSpeciality: $speciality"
                    }
                } else {
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
            }
        }
    }

    private fun doUpdate() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                btnUpdate.visibility = View.INVISIBLE
                updateProgress.visibility = View.VISIBLE
                val response = ApiAdapter.apiClient.updateDocDetails(
                    "$type $token",
                    DetailsUpdateData(
                        Details(
                            docSpeciality.text.toString(),
                            docWorksAt.text.toString()
                        )
                    )
                )
                if (response.isSuccessful && response.body() != null) {
                    updateProgress.visibility = View.INVISIBLE
                    toast("Details updated successfully")
                    finish()
                } else {
                    btnUpdate.visibility = View.INVISIBLE
                    toast(response.message().toString())
                }
            } catch (e: Exception) {
                toast(e.message.toString())
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
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.putExtra("EXIT", true)
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, which ->
                //
            }
            .show()
    }

}