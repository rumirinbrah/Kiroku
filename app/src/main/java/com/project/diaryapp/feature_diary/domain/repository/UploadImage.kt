package com.project.diaryapp.feature_diary.domain.repository

import com.project.diaryapp.feature_diary.domain.model.ImageToUpload

interface UploadImage {

    suspend fun getAllImages() : List<ImageToUpload>

    suspend fun addImageToUpload(imageToUpload: ImageToUpload)

    suspend fun cleanUpImage(imageId : Int)

}