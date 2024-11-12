package com.sanpc.roadsense.ui.navigation

sealed class Screen (val route: String){
    data object Home: Screen("Home")
    data object Profile: Screen("Profile")
    data object Map: Screen("Map")
    data object Reports: Screen("Reports")
}
