package com.sanpc.roadsense.ui.screen

sealed class Screen (val screen: String){
    data object Home: Screen("Home")
    data object Profile: Screen("Profile")
    data object Map: Screen("Map")
    data object Reports: Screen("Reports")
}
