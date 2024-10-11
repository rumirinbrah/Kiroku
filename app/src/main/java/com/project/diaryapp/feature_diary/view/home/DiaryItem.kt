package com.project.diaryapp.feature_diary.view.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.domain.model.Mood
import com.project.diaryapp.feature_diary.util.toLocalDate
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun DiaryItem(
    diary: Diary ,
    onClick: (String) -> Unit //its the diary id
) {
    var lineHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Row(
        Modifier
            .clickable(
                indication = null ,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(diary.id.toString())
            }
            .padding(end = 5.dp)
    ) {

        Spacer(Modifier.width(14.dp))
        //straight line
        Surface(
            Modifier
                .width(2.dp)
                .height(lineHeight + 10.dp) ,
            tonalElevation = 3.dp
        ) {}
        Spacer(Modifier.width(10.dp))

        Surface(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .clip(Shapes().medium)
                .onGloballyPositioned {
                    lineHeight = with(density) {
                        it.size.height.toDp()
                    }
                }
                ,
            tonalElevation = 1.dp,
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding()
                    .background(MaterialTheme.colorScheme.onBackground)
            ){
                DiaryHeader(moodName = diary.mood, date = diary.date)

                Text(
                    modifier=Modifier.padding(10.dp),
                    text = diary.description ,
                    maxLines = 6 ,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.background
                )

                //GALLERY OPTIONS
                if(!diary.images.isNullOrEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))

                    GalleryButton(diary.images)
                }
            }
        }
    }
}

@Composable
fun DiaryHeader(moodName : String , date: Date) {

    val format = SimpleDateFormat("hh:mm a", Locale.US)
    val time = format.format(date)
    val mood = Mood.valueOf(moodName)
    //insaaaaaaaaaaaaaaaaaaane
    Row (
        Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 5.dp , vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = mood.icon) ,
                contentDescription = null,
                modifier=Modifier.size(18.dp)
            )

            Spacer(Modifier.width(10.dp))

            Text(text = mood.name,color=mood.contentColor) //returns object name
            
        }

        Text(text = time,color = mood.contentColor)
    }
}

@Composable
fun GalleryButton(
    images : List<String>
) {
    var isVisible by remember{ mutableStateOf(false) }
    Column {
        TextButton(onClick = { isVisible=!isVisible }) {
            Text(text = if(isVisible) "Hide Gallery" else "Show Gallery", color = MaterialTheme.colorScheme.background)
        }
        AnimatedVisibility(visible = isVisible) {
            GalleryRow(images = images, modifier = Modifier.padding(10.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))

    }
}



