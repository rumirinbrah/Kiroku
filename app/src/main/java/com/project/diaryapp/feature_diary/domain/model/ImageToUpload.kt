package com.project.diaryapp.feature_diary.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imageUploadTable")
data class ImageToUpload(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val localPath : String,
    val imageUri : String,
    val sessionUri : String
)
