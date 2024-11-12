package com.sanpc.roadsense.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sanpc.roadsense.ui.screen.Home
import com.sanpc.roadsense.ui.screen.Login
import com.sanpc.roadsense.ui.screen.Map
import com.sanpc.roadsense.ui.screen.Profile
import com.sanpc.roadsense.ui.screen.Register
import com.sanpc.roadsense.ui.screen.Reports
import com.sanpc.roadsense.utils.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext

@Composable
fun AppNavigation(@ApplicationContext context: Context) {
    val navController = rememberNavController()
    val userPreferences = UserPreferences(context)

    val startDestination = if (userPreferences.isLoggedIn) Routes.HOME else Routes.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable(Routes.HOME){ Home() }

            composable(Routes.PROFILE){ Profile(
                username = userPreferences.username,
                email = userPreferences.email,
                pass = userPreferences.password,
                navController = navController
            ) }

            composable(Routes.MAP){ Map() }

            composable(Routes.REPORTS){ Reports() }

            composable(Routes.LOGIN) { Login(
                context = context,
                navController = navController
            ) }

            composable(Routes.REGISTER){ Register(
                context = context,
                navController = navController
            ) }
        }
    )
}
