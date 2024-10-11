package com.project.diaryapp.feature_diary.domain.model

import androidx.compose.ui.graphics.Color
import com.project.diaryapp.R

enum class Mood (
    val icon : Int,
    val containerColor : Color,
    val contentColor : Color //the text color
){
    Happy(
        icon = R.drawable.happy_emoji,
        contentColor = Color.Black,
        containerColor = Color(0xFFEBDE6A)
    ),
    Sad(
    icon = R.drawable.sad_emoji,
    contentColor = Color.White,
    containerColor = Color.Black
    ),
    Surprised(
        icon = R.drawable.surprised_emoji,
        contentColor = Color.White,
        containerColor = Color(0xFF3F51B5)
    ),
    Angry(
        icon = R.drawable.angry_emoji,
        contentColor = Color.White,
        containerColor = Color(0xFFA2190F)
    )
}