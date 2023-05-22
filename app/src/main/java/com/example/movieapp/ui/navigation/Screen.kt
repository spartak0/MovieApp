package com.example.movieapp.ui.navigation

sealed class Screen(val route:String){
    object MainScreen:Screen(route="main_screen")
    object SplashScreen:Screen(route="splash_screen")
    object DetailsScreen:Screen(route="details_screen")
    object SearchScreen:Screen(route="search_screen")
}
