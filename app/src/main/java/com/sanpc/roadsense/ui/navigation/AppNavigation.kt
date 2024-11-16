package com.sanpc.roadsense.ui.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sanpc.roadsense.R
import com.sanpc.roadsense.data.model.Drop
import com.sanpc.roadsense.data.model.Pothole
import com.sanpc.roadsense.ui.screen.*
import com.sanpc.roadsense.ui.theme.Orange
import com.sanpc.roadsense.ui.viewmodel.*
import com.sanpc.roadsense.utils.MQTTClient
import com.sanpc.roadsense.utils.UserPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(
    context: Context,
    loginViewModel: LoginViewModel,
    potholeViewModel: PotholeViewModel,
    dropViewModel: DropViewModel,
    locationViewModel: LocationViewModel
) {
    val navController = rememberNavController()
    val userPreferences = UserPreferences(context)

    val startDestination = if (userPreferences.isLoggedIn) Routes.HOME else Routes.LOGIN
    val currentDestination by navController.currentBackStackEntryAsState()
    val showBars = currentDestination?.destination?.route != Routes.LOGIN

    var potholes by remember { mutableStateOf<List<Pothole>>(emptyList()) }
    var drops by remember { mutableStateOf<List<Drop>>(emptyList()) }

    val mqttClient = MQTTClient(
        username = userPreferences.username,
        dropViewModel = dropViewModel,
        potholeViewModel = potholeViewModel
    )

    LaunchedEffect(true) {
        potholes = potholeViewModel.getPotholesByUsername(userPreferences.username)
        drops = dropViewModel.getDropsByUsername(userPreferences.username)
    }

    Scaffold(
        topBar = {
            if (showBars) {
                TopBar()
            }
        },
        bottomBar = {
            if (showBars) {
                BottomBar(navController) {
                    Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show()
                    CoroutineScope(Dispatchers.IO).launch {
                        mqttClient.connectAndSyncData()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            builder = {
                composable(Routes.HOME) {
                    Home(
                        context = context,
                        locationViewModel = locationViewModel
                    )
                }
                composable(Routes.MAP) {
                    Map(
                        context = context,
                        potholes = potholes,
                        drops = drops,
                        locationViewModel = locationViewModel
                    )
                }
                composable(Routes.PROFILE) {
                    Profile(
                        username = userPreferences.username,
                        email = userPreferences.email,
                        pass = userPreferences.password,
                        loginViewModel = loginViewModel,
                        navController = navController
                    )
                }
                composable(Routes.REPORTS) {
                    Reports(
                        potholes = potholes,
                        drops = drops
                    )
                }
                composable(Routes.LOGIN) {
                    Login(
                        context = context,
                        navController = navController,
                        loginViewModel = loginViewModel
                    )
                }
                composable(Routes.REGISTER) {
                    Register(
                        context = context,
                        navController = navController
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    val context = LocalContext.current.applicationContext

    TopAppBar(
        title = { Text(text = "RoadSense") },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "RoadSense Icon"
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Orange,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        ),
        actions = {
            IconButton(
                onClick = { Toast.makeText(context, "Created by Federico Chiodi for Lab. Ind. 4.0 @UniFE - 2024", Toast.LENGTH_SHORT).show() }
            ) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "Info", tint = Color.White)
            }
        }
    )
}

@Composable
fun BottomBar(navigationController: NavController, onSendClick: () -> Unit) {
    val selected = remember { mutableStateOf(Icons.Default.Home) }

    BottomAppBar(
        containerColor = Orange
    ) {
        IconButton(
            onClick = {
                selected.value = Icons.Default.Home
                navigationController.navigate(Routes.HOME) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Home, contentDescription = null, modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
            )
        }

        IconButton(
            onClick = {
                selected.value = Icons.Default.Place
                navigationController.navigate(Routes.MAP) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Place, contentDescription = null, modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.Place) Color.White else Color.DarkGray
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = onSendClick,
                containerColor = Color.White,
                contentColor = Orange
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = Orange)
            }
        }

        IconButton(
            onClick = {
                selected.value = Icons.Default.DateRange
                navigationController.navigate(Routes.REPORTS) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.DateRange) Color.White else Color.DarkGray
            )
        }

        IconButton(
            onClick = {
                selected.value = Icons.Default.Person
                navigationController.navigate(Routes.PROFILE) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                Icons.Default.Person, contentDescription = null, modifier = Modifier.size(26.dp),
                tint = if (selected.value == Icons.Default.Person) Color.White else Color.DarkGray
            )
        }
    }
}
