package com.project.diaryapp.feature_diary.util

sealed class Screen(val route : String) {

    object AuthenticationScreen : Screen("auth_screen")
    object HomeScreen : Screen("home_screen")
    object CreateScreen : Screen("create_screen")


}