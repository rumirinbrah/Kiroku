package com.project.diaryapp.feature_diary.data.repository

import com.project.diaryapp.feature_diary.data.source.ImageToUploadDao
import com.project.diaryapp.feature_diary.domain.model.ImageToUpload
import com.project.diaryapp.feature_diary.domain.repository.UploadImage

class UploadImageImpl(
    private val imageToUploadDao: ImageToUploadDao
) : UploadImage {
    override suspend fun getAllImages(): List<ImageToUpload> {
        return imageToUploadDao.getAllImages()
    }

    override suspend fun addImageToUpload(imageToUpload: ImageToUpload) {
        imageToUploadDao.addImageToUpload(imageToUpload)
    }

    override suspend fun cleanUpImage(imageId: Int) {
        imageToUploadDao.cleanUpImage(imageId)
    }
}