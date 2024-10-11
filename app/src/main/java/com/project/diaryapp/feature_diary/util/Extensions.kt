package com.project.diaryapp.feature_diary.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

fun Instant.toLocalDate() :LocalDate{
   return this.atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Date.toFormattedDate():String{
   val format = SimpleDateFormat("hh:mm a", Locale.US)
   return format.format(this)
}

