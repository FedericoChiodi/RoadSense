package com.sanpc.roadsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import com.sanpc.roadsense.ui.navigation.AppNavigation
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.LocationViewModel
import com.sanpc.roadsense.ui.viewmodel.LoginViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val potholeViewModel: PotholeViewModel by viewModels()
    private val dropViewModel: DropViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme()
            )
            {
                AppNavigation(
                    context = applicationContext,
                    loginViewModel = loginViewModel,
                    potholeViewModel = potholeViewModel,
                    dropViewModel = dropViewModel,
                    locationViewModel = locationViewModel
                )
            }
        }
    }
}
