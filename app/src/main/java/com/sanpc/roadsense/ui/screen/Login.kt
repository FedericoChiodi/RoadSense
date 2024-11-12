package com.sanpc.roadsense.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
    ){

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = ContentScale.FillBounds
            )
            .zIndex(-1f)
        )
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 26.dp, vertical = 80.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp)
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
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    loginViewModel.login(username, password,
                        onSuccess = { navController.navigate(Routes.HOME) },
                        onFailure = { Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show() }
                    )
                },
                colors = ButtonDefaults.buttonColors(Orange),
                modifier = Modifier.padding(top = 18.dp)
            ) {
                Text(text = "Login", fontSize = 22.sp)
            }

            TextButton(
                onClick = { navController.navigate(Routes.REGISTER) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Non hai un account? Registrati", color = Orange)
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview(){
    RoadSenseTheme {

    }
}