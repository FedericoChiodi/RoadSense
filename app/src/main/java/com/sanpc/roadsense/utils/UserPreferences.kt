package com.sanpc.roadsense.utils

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

class UserPreferences(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var username: String
        get() = sharedPreferences.getString("username", "").toString()
        set(value) {
            sharedPreferences.edit().putString("username", value).apply()
        }

    var email: String
        get() = sharedPreferences.getString("email", "").toString()
        set(value) {
            sharedPreferences.edit().putString("email", value).apply()
        }

    var password: String
        get() = sharedPreferences.getString("password", "").toString()
        set(value) {
            sharedPreferences.edit().putString("password", value).apply()
        }

    var isLoggedIn: Boolean
        get() = sharedPreferences.getBoolean("isLoggedIn", false)
        set(value) {
            sharedPreferences.edit().putBoolean("isLoggedIn", value).apply()
        }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
