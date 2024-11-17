package com.sanpc.roadsense.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.sanpc.roadsense.R
import com.sanpc.roadsense.ui.navigation.Routes
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.LoginViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel

@Composable
fun Profile(
    navController: NavController,
    username: String,
    email: String,
    pass: String,
    loginViewModel: LoginViewModel,
    potholeViewModel: PotholeViewModel,
    dropViewModel: DropViewModel
) {
    val (usernameState, setUsername) = remember { mutableStateOf(TextFieldValue(username)) }
    val (passState, setPass) = remember { mutableStateOf(TextFieldValue(pass)) }
    val (emailState, setEmail) = remember { mutableStateOf(TextFieldValue(email)) }
    val showDialog = remember { mutableStateOf(false) }

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
                visualTransformation = PasswordVisualTransformation(),
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
                onClick = {
                    loginViewModel.logout()
                    navController.navigate(Routes.LOGIN)
                },
                modifier = Modifier
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(Orange)
            ) {
                Text("Logout", color = Color.White)
            }

            Button(
                onClick = {
                    showDialog.value = true
                },
                modifier = Modifier
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(Orange)
            ) {
                Text("Delete my Reports", color = Color.White)
            }
        }

        // Dialogo di conferma
        if (showDialog.value) {
            ConfirmDialog(
                onDismiss = {
                    showDialog.value = false
                },
                onConfirm = {
                    potholeViewModel.clearPotholes()
                    dropViewModel.clearDrops()
                    Toast.makeText(navController.context, "All reports deleted!", Toast.LENGTH_SHORT).show()
                    showDialog.value = false
                }
            )
        }
    }
}

@Composable
fun ConfirmDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .height(230.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon),
                    modifier = Modifier.size(75.dp),
                    contentDescription = "RoadSense Icon",
                    tint = Orange
                )
                Text(
                    text = "Are you sure?",
                    modifier = Modifier.padding(top = 5.dp, bottom = 25.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Yes")
                    }

                    Spacer(modifier = Modifier.padding(14.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Orange,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "No")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    RoadSenseTheme {
    }
}

