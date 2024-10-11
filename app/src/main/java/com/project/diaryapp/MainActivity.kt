package com.project.diaryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.project.diaryapp.feature_diary.view.create.CreateDiaryViewModel
import com.project.diaryapp.feature_diary.view.home.HomeViewModel
import com.project.diaryapp.feature_diary.view.navigation.Navigation
import com.project.diaryapp.feature_diary.view.one_tap.GoogleAuthClient
import com.project.diaryapp.feature_diary.view.one_tap.OneTapViewModel
import com.project.diaryapp.ui.theme.DiaryTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @Inject
    lateinit var googleAuthClient: GoogleAuthClient

    @Inject
    lateinit var oneTapViewModel: OneTapViewModel

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var createDiaryViewModel: CreateDiaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            DiaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize() ,
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Navigation(
                        googleAuthClient = googleAuthClient ,
                        oneTapViewModel = oneTapViewModel ,
                        homeViewModel = homeViewModel ,
                        createDiaryViewModel = createDiaryViewModel ,
                        navController = navController
                    )

                }
            }
        }
    }
}
