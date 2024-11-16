package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sanpc.roadsense.R
import com.sanpc.roadsense.ui.navigation.Routes
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import com.sanpc.roadsense.ui.viewmodel.LoginViewModel

@Composable
fun Login(
    context: Context,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "RoadSense Icon",
                tint = Orange,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = "RoadSense",
                color = Orange,
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedLeadingIconColor = Orange,
                    unfocusedLeadingIconColor = Orange,
                    focusedLabelColor = Orange,
                    unfocusedLabelColor = Orange,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Orange,
                    unfocusedIndicatorColor = Orange
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(20.dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedLeadingIconColor = Orange,
                    unfocusedLeadingIconColor = Orange,
                    focusedLabelColor = Orange,
                    unfocusedLabelColor = Orange,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Orange,
                    unfocusedIndicatorColor = Orange
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            keyboardController?.hide()
                        }
                    }
            )

            Button(
                onClick = {
                    loginViewModel.login(username, password,
                        onSuccess = {
                            keyboardController?.hide()
                            navController.navigate(Routes.HOME)
                        },
                        onFailure = { Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show() }
                    )
                },
                colors = ButtonDefaults.buttonColors(Orange),
                modifier = Modifier.padding(top = 18.dp)
            ) {
                Text(text = "Login", fontSize = 22.sp)
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview() {
    RoadSenseTheme {
    }
}

