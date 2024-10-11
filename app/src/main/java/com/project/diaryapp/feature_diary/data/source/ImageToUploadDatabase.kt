package com.project.diaryapp.feature_diary.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.diaryapp.feature_diary.domain.model.ImageToUpload

@Database(
    entities = [ImageToUpload::class],
    version = 1,
    exportSchema = false
)
abstract class ImageToUploadDatabase :RoomDatabase(){

    abstract val imageToUploadDao : ImageToUploadDao

}