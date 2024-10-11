package com.project.diaryapp.feature_diary.view.create

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.project.diaryapp.feature_diary.domain.model.GalleryState
import com.project.diaryapp.feature_diary.domain.model.Mood
import com.project.diaryapp.feature_diary.util.RequestState
import java.util.Date

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CreateDiaryPage(
    pagerState: com.google.accompanist.pager.PagerState ,
    galleryState: GalleryState,
    navController: NavController ,
    buttonEnabled : Boolean,
    uiState: DiaryState ,
    onImageSelect :(Uri) ->Unit,
    moodName: () -> String ,
    onTitleChange: (String) -> Unit ,
    onDescriptionChange: (String) -> Unit ,
    onNavigateUp: () -> Unit ,
    onSaveDiary: () -> Unit ,
) {

    val focusManager = LocalFocusManager.current

    LaunchedEffect(uiState.mood) {
        if(uiState.loading is RequestState.Idle ){
            pagerState.scrollToPage(Mood.valueOf(uiState.mood).ordinal)
        }
    }


    if (uiState.loading is RequestState.Loading) {
        ProgressIndicator()
    }else {
        Scaffold(
            topBar = {
                CreatePageTopBar(
                    date = uiState.currentDiary?.date ?: Date() ,
                    moodName = moodName ,
                    newDiary = uiState.currentDiary == null ,
                    navigateBack = {
                        navController.navigateUp()
                        onNavigateUp()
                    } ,
                    onDelete = {}
                )
            } ,
            backgroundColor = MaterialTheme.colorScheme.background
        ) {

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)

            ) {
                Spacer(modifier = Modifier.height(20.dp))

                MoodSelector(
                    pagerState = pagerState ,
                    modifier = Modifier
                )

                CreateTextBox(
                    modifier = Modifier.weight(1f) ,
                    focusManager = focusManager,
                    diaryTitle = uiState.title ,
                    diaryDescription = uiState.description ,
                    onTitleChange = { title ->
                        onTitleChange(title)
                    } ,
                    onDescriptionChange = { desc ->
                        onDescriptionChange(desc)
                    }
                )


                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp , vertical = 5.dp) ,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    GalleryUploader(
                        galleryState = galleryState  ,
                        onAdd = {
                                focusManager.clearFocus()
                        } ,
                        onImageSelect = {uri->
                                        onImageSelect(uri)
                        } ,
                        onImageClick = {}
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    //SAVE
                    Button(
                        enabled = buttonEnabled,
                        onClick = {
                            if (uiState.title.isNotEmpty()) {
                                onSaveDiary()
                            }
                        } ,
                        modifier = Modifier.fillMaxWidth() ,
                        shape = Shapes().small
                    ) {
                        Text(text = "Save")
                    }
                    if(!buttonEnabled){
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    }
                }

            }

        }
    }

}

@Composable
fun ProgressIndicator() {
    Row(
        Modifier.fillMaxWidth() ,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}