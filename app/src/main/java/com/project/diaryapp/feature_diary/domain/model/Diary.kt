package com.project.diaryapp.feature_diary.domain.model

import java.time.Instant
import java.time.LocalDate
import java.util.Date


data class Diary  (

    val id : String= "" ,// id for each Diary
    val mood : String = Mood.Happy.name ,  // realm doesn't support enum classes
    val title : String = "" ,
    val description : String = "" ,
    val images : List<String>? = null ,
    val date : Date = Date()
)

