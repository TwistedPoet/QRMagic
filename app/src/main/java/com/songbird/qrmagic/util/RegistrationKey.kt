package com.songbird.qrmagic.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


object RegistrationKey {
    private const val PREF_NAME = "qrmagic_prefs"
    private const val KEY_IS_REGISTERED = "is_registered"


    fun isRegistered(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_REGISTERED, false)
    }

    fun setRegistered(context: Context, value: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit {
            putBoolean(KEY_IS_REGISTERED, value)
        }
    }

    fun getValidPrefixes(): List<String> {
        return listOf(
            listOf('q','r','m','a','g','i','c','.','p','r','o').joinToString(""),
            listOf('q','r','m','a','g','i','c','.','r','e','g').joinToString(""),
            listOf('q','r','m','a','g','i','c','.','l','i','c').joinToString("")
        )
    }

    fun isValidUnlockCode(code: String): Boolean {
        val parts = code.split(".")
        if (parts.size < 3) return false
        val prefix = parts[0] + "." + parts[1]
        val suffix = parts.drop(2).joinToString(".")

        return getValidPrefixes().contains(prefix) && suffix.length == 10
    }

}