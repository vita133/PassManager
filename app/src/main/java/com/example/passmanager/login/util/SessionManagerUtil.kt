package com.example.passmanager.login.util

import android.content.Context
import java.util.*

object SessionManagerUtil {

    const val SESSION_PREFERENCES = "ke.co.skylinedesign.session_manager.SESSION_PREFERENCES"
    const val SESSION_TOKEN = "ke.co.skylinedesign.session_manager.SESSION_TOKEN"
    const val SESSION_EXPIRY_TIME = "ke.co.skylinedesign.session_manager.SESSION_EXPIRY_TIME"

    fun startUserSession(context: Context, expiresIn: Int) {
        val calendar = Calendar.getInstance()
        val userLoggedInTime = calendar.time
        calendar.time = userLoggedInTime
        calendar.add(Calendar.SECOND, expiresIn)
        val expiryTime = calendar.time
        val tokenSharedPreferences = context.getSharedPreferences(SESSION_PREFERENCES, 0)
        val editor = tokenSharedPreferences.edit()
        editor.putLong(SESSION_EXPIRY_TIME, expiryTime.time)
        editor.apply()
    }

    fun isSessionActive(currentTime: Date, context: Context): Boolean {
        val sessionExpiresAt = getExpiryDateFromPreferences(context)?.let { Date(it) }
        return currentTime.before(sessionExpiresAt)
    }


    private fun getExpiryDateFromPreferences(context: Context) : Long? {
        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getLong(SESSION_EXPIRY_TIME, 0)
    }

    fun storeUserToken(context: Context, token: String) {
        val hashedToken = HashUtils.sha256Hash(token)
        val tokenEditor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        tokenEditor.putString(SESSION_TOKEN, hashedToken)
        tokenEditor.apply()
    }

    fun getUserToken(context: Context) : String? {
        return context.getSharedPreferences(SESSION_PREFERENCES, 0).getString(SESSION_TOKEN, "")
    }

    fun endUserSession(context: Context) {
        clearStoredData(context)
    }

    private fun clearStoredData(context: Context) {
        val editor = context.getSharedPreferences(SESSION_PREFERENCES, 0).edit()
        editor.clear()
        editor.apply()
    }
}