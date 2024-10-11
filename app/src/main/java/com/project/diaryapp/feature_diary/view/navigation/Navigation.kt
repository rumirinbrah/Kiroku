package com.project.diaryapp.feature_diary.view.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.domain.model.Mood
import com.project.diaryapp.feature_diary.util.Screen
import com.project.diaryapp.feature_diary.view.create.CreateDiaryPage
import com.project.diaryapp.feature_diary.view.create.CreateDiaryViewModel
import com.project.diaryapp.feature_diary.view.home.HomePage
import com.project.diaryapp.feature_diary.view.home.HomeViewModel
import com.project.diaryapp.feature_diary.view.one_tap.GoogleAuthClient
import com.project.diaryapp.feature_diary.view.one_tap.OneTapSignIn
import com.project.diaryapp.feature_diary.view.one_tap.OneTapViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Navigation(
    googleAuthClient: GoogleAuthClient ,
    oneTapViewModel: OneTapViewModel ,
    homeViewModel: HomeViewModel ,
    createDiaryViewModel: CreateDiaryViewModel ,
    navController: NavHostController ,
) {
    val signInState by oneTapViewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState( initialPage = 0)
    val pageNumber by remember{
        derivedStateOf {
            pagerState.currentPage
        }
    }
    val uiState by createDiaryViewModel.uiDiaryState

    val diaries by homeViewModel.diaries.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    oneTapViewModel.onSignInResult(signInResult)
                }
            }
        }

    val startDestination =
        if (googleAuthClient.getCurrentUser() != null) Screen.HomeScreen.route else Screen.AuthenticationScreen.route

    LaunchedEffect(signInState.isSignInSuccessful) {
        if (signInState.isSignInSuccessful) {
            navController.navigate(Screen.HomeScreen.route)
            oneTapViewModel.resetState()
        }
    }

    NavHost(
        navController = navController ,
        startDestination = startDestination
    ) {

        //AUTH
        composable(Screen.AuthenticationScreen.route) {
            OneTapSignIn(
                user = googleAuthClient.getCurrentUser() ,
                signInState = signInState ,
                onSignIn = {
                    scope.launch {
                        val signInIntentSender = googleAuthClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }

        //HOME
        composable(Screen.HomeScreen.route) {
            HomePage(
                userData = googleAuthClient.getCurrentUser() ,
                navController = navController ,
                diaries = diaries,
                onSignOut = {
                    scope.launch {
                        googleAuthClient.logOut()
                        oneTapViewModel.resetState()
                        navController.navigate(Screen.AuthenticationScreen.route) {
                            popUpTo(Screen.HomeScreen.route) {
                                inclusive = true
                            }
                        }
                    }
                },
                refresh = {
                    homeViewModel.refreshDiaries()
                }
            )
        }

        //CREATE
        composable(
            route = Screen.CreateScreen.route + "/{id}" ,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) {

            val id = it.arguments?.getString("id")
            val galleryState by createDiaryViewModel.galleryState
            var buttonEnabled by remember{ mutableStateOf(true) }


            LaunchedEffect(Unit )
            {
                if (id != null) {
                    createDiaryViewModel.setId(id)
                }
            }

            CreateDiaryPage(
                pagerState = pagerState,
                galleryState = galleryState,
                uiState = uiState ,
                navController = navController ,
                buttonEnabled = buttonEnabled,
                onImageSelect = {uri->
                    val type = context.contentResolver.getType(uri)?.split("/")?.last() ?: "jpg"
                    createDiaryViewModel.addImageToState(
                        image = uri ,
                        type = type
                    )
                },
                moodName = {
                    Mood.entries[pageNumber].name
                },
                onTitleChange = { title ->
                    createDiaryViewModel.setTitle(title)
                } ,
                onDescriptionChange = { desc ->
                    createDiaryViewModel.setDescription(desc)

                },
                onNavigateUp = {
                    createDiaryViewModel.resetState()
                }
            ) {

                if(uiState.currentDiaryId!=null){
                    buttonEnabled = true
                    createDiaryViewModel.updateDiary(
                        Diary(
                            id= uiState.currentDiaryId!!,
                            title = uiState.title,
                            description = uiState.description,
                            date = uiState.currentDiary!!.date,
                            mood = Mood.entries[pageNumber].name
                        )
                    )
                    navController.navigateUp()
                }else {
                    buttonEnabled = false
                    println("INSIDE NAVIGATION ${galleryState.images.size}")
                    createDiaryViewModel.addDiaryToDatabase(
                        Diary(
                            title = uiState.title ,
                            description = uiState.description ,
                            mood = Mood.entries[pageNumber].name
                        ),
                        onDone = {
                            navController.navigateUp()
                            buttonEnabled = true
                        }
                    )

                }
            }

        }

    }

}




