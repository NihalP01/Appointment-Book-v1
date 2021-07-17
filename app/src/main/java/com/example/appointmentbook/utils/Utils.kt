package com.example.appointmentbook.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.appointmentbook.utils.Utils.Companion.clearPreferences
import com.example.appointmentbook.utils.Utils.Companion.getRole
import com.example.appointmentbook.utils.Utils.Companion.logout
import com.google.firebase.messaging.FirebaseMessaging
import kotlin.Exception

class Utils {


    companion object {

        private const val LOGGED_KEY = "login"
        const val ROLE_KEY = "role"
        const val ID_KEY = "id"
        const val TOKEN_KEY = "token"
        const val AUTH_TYPE = "type"
        const val USER_NAME = "userName"
        const val USER_EMAIL = "userEmail"
        const val DOC_ID = "doc_id"
        const val SLOT_ID = "slotId"

        private var _preferences: SharedPreferences? = null

        private fun getSharedPreferences(context: Context): SharedPreferences {
            if (_preferences == null)
                _preferences = context.applicationContext.getSharedPreferences(
                    "app_pref",
                    MODE_PRIVATE
                )
            return _preferences!!
        }

        fun Context.getPreference() = getSharedPreferences(this)

        fun setLogged(context: Context, v: Boolean) {
            getSharedPreferences(context).edit {
                putBoolean(LOGGED_KEY, v)
            }
        }

        @JvmName("setLogged1")
        fun Context.setLogged(v: Boolean) {
            setLogged(this, v)
        }

        fun Context.logout() = _logout(this)

        private fun _logout(context: Context) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(context.getRole())
            FirebaseMessaging.getInstance().unsubscribeFromTopic(context.getId().toString())
            setLogged(context, false)
        }

        fun getLogged(context: Context) =
            getSharedPreferences(context).getBoolean(LOGGED_KEY, false)

        @JvmName("getLogged1")
        fun Context.getLogged(): Boolean {
            return getLogged(this)
        }

        fun Context.getFromPref(key: String): String {
            val pref = getSharedPreferences(this)
            if (!pref.contains(key)) throw Exception("Key not found")
            return pref.getString(key, "").toString()
        }

        fun Context.clearPreferences() {
            getSharedPreferences(this).edit {
                clear()
            }
        }

        fun Context.startActivity(intent: Class<*>) {
            this.startActivity(Intent(this, intent))
        }

        fun Context.getRole(): String {
            if (!getSharedPreferences(this).contains(ROLE_KEY)) throw Exception("$ROLE_KEY not found")
            return getSharedPreferences(this).getString(ROLE_KEY, "").toString()
        }

        fun Context.getId(): Int {
            if (!getSharedPreferences(this).contains(ID_KEY)) throw Exception("$ID_KEY not found")
            return getSharedPreferences(this).getInt(ID_KEY, -1)
        }

        fun Context.docId(id: String): String {
            val pref = getSharedPreferences(this)
            if (!pref.contains(id)) throw Exception("Id not found")
            return pref.getString(id, "").toString()
        }

        fun Context.getToken(token: String): String {
            val pref = getSharedPreferences(this)
            if (!pref.contains(token)) throw Exception("Token not found")
            return pref.getString(token, "").toString()
        }

        fun Context.getAuthType(type: String): String {
            val pref = getSharedPreferences(this)
            if (!pref.contains(type)) throw Exception("Auth type not found")
            return pref.getString(type, "").toString()
        }

        fun Context.getUserName(userName: String): String{
            val pref = getSharedPreferences(this)
            if (!pref.contains(userName)) throw Exception("User Name not found")
            return pref.getString(userName, "").toString()
        }

        fun Context.getSlotId(slotId: String) : String {
            val pref = getSharedPreferences(this)
            if (!pref.contains(slotId)) throw Exception("Slot id not found")
            return pref.getString(slotId, "").toString()
        }

        fun Context.subscribeToRole() {
            FirebaseMessaging.getInstance().subscribeToTopic(getRole())
        }

        fun Context.subscribeToId() {
            //FirebaseMessaging.getInstance().subscribeToTopic()
        }

        fun Context.subscribeToTopic(topic: String) {
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
        }

    }

}