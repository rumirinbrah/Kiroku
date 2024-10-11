package com.project.diaryapp.feature_diary.domain.model

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

//now you know <:
@Composable
fun rememberGalleryState() : GalleryState {
    return remember { GalleryState() }
}

class GalleryState {

    val images = mutableStateListOf<GalleryImage>()
    val imagesToBeDeleted = mutableStateListOf<GalleryImage>()

    fun addImage(image : GalleryImage){
        images.add(image)
    }

    fun removeImage(image: GalleryImage){
        images.remove(image)
        imagesToBeDeleted.add(image)
    }

    fun resetDeletable(){
        imagesToBeDeleted.clear()
    }
}

/***
 *Represents a single image within the gallery
 * @param image The local image URI
 * @param remoteUrl Remote image path
 */
data class GalleryImage(
    val image : Uri,
    val remoteUrl : String = ""
)