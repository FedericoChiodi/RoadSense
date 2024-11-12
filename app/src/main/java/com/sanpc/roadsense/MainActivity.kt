package com.sanpc.roadsense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.sanpc.roadsense.ui.navigation.AppNavigation
import com.sanpc.roadsense.ui.theme.RoadSenseTheme
import com.sanpc.roadsense.ui.viewmodel.DropViewModel
import com.sanpc.roadsense.ui.viewmodel.LoginViewModel
import com.sanpc.roadsense.ui.viewmodel.PotholeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val potholeViewModel: PotholeViewModel by viewModels()
    private val dropViewModel: DropViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoadSenseTheme {
                AppNavigation(
                    context = applicationContext,
                    loginViewModel = loginViewModel,
                    potholeViewModel = potholeViewModel,
                    dropViewModel = dropViewModel
                )
            }
        }
    }
}
