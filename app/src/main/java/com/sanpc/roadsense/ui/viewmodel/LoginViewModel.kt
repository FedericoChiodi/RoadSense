package com.sanpc.roadsense.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.sanpc.roadsense.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {

    private val userPreferences = UserPreferences(context)

    private val validCredentials = listOf(
        Triple("fc", "pass", "federico.chiodi@edu.unife.it"),
        Triple("cg", "pass", "cg@unife.it"),
        Triple("mt", "pass", "mt@unife.it"),
        Triple("pv", "pass", "pv@gmail.com"),
        Triple("dc", "pass", "dc@gmail.com"),
        Triple("mb", "buga!", "mb@gmail.com"),
        Triple("ff", "bang!", "ff@gmail.com"),
        Triple("eb", "blast!", "eb@gmail.com")
    )

    // POC - in production this checks users from DB and gets info from there
    fun login(username: String, password: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val credentials = validCredentials.find { it.first == username && it.second == password }

        if (credentials != null) {
            userPreferences.username = username
            userPreferences.password = password
            userPreferences.email = credentials.third
            userPreferences.isLoggedIn = true
            onSuccess()
        } else {
            onFailure()
        }
    }

    fun logout() {
        userPreferences.clear()
    }
}
