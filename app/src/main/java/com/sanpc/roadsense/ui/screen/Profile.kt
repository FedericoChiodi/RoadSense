package com.sanpc.roadsense.ui.screen

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun Profile(username: String, email: String, pass: String, @ApplicationContext context: Context) {
    val (usernameState, setUsername) = remember { mutableStateOf(TextFieldValue(username)) }
    val (passState, setPass) = remember { mutableStateOf(TextFieldValue(pass)) }
    val (emailState, setEmail) = remember { mutableStateOf(TextFieldValue(email)) }

    fun logout() {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("is_logged_in")
            remove("username")
            remove("email")
            remove("password")
            apply()
        }

        Toast.makeText(context, "Goodbye!", Toast.LENGTH_SHORT).show()

        (context as? Activity)?.finish()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Profile", fontSize = 30.sp, color = Orange)

            OutlinedTextField(
                value = usernameState,
                onValueChange = setUsername,
                label = { Text("Username", color = Orange) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Orange,
                    unfocusedIndicatorColor = Orange,
                    cursorColor = Orange,
                    focusedTextColor = Orange,
                    unfocusedTextColor = Orange
                ),
                modifier = Modifier.padding(top = 16.dp)
            )

            OutlinedTextField(
                value = passState,
                onValueChange = setPass,
                label = { Text("Password", color = Orange) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Orange,
                    unfocusedIndicatorColor = Orange,
                    cursorColor = Orange,
                    focusedTextColor = Orange,
                    unfocusedTextColor = Orange
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            OutlinedTextField(
                value = emailState,
                onValueChange = setEmail,
                label = { Text("Email", color = Orange) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Orange,
                    unfocusedIndicatorColor = Orange,
                    cursorColor = Orange,
                    focusedTextColor = Orange,
                    unfocusedTextColor = Orange
                ),
                modifier = Modifier.padding(top = 8.dp)
            )

            Button(
                onClick = { logout() },
                modifier = Modifier
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(Orange)
            ) {
                Text("Logout", color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    RoadSenseTheme {
        Profile(username = "User123", email = "user@example.com", pass = "1234567890", context = LocalContext.current)
    }
}
