package com.project.diaryapp.feature_diary.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.diaryapp.feature_diary.domain.model.ImageToUpload

@Dao
abstract class ImageToUploadDao {

    @Query("select * from imageUploadTable ORDER BY id ASC")
    abstract suspend fun getAllImages() :List<ImageToUpload>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addImageToUpload(imageToUpload: ImageToUpload)

    @Query("delete from imageUploadTable where id = :id")
    abstract suspend fun cleanUpImage(id : Int)

}