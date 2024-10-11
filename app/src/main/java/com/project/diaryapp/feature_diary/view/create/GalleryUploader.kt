package com.project.diaryapp.feature_diary.view.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.project.diaryapp.feature_diary.domain.model.GalleryImage
import com.project.diaryapp.feature_diary.domain.model.GalleryState
import kotlin.math.max

@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier ,
    galleryState: GalleryState ,
    imageSize: Dp = 60.dp ,
    shape: Shape = Shapes().small ,
    spaceBetween: Dp = 10.dp ,
    onAdd: () -> Unit ,
    onImageSelect: (Uri) -> Unit ,
    onImageClick: (GalleryImage) -> Unit
) {

    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(6)
    ) { images ->
        images.onEach {
            onImageSelect(it)
        }
    }

    val context = LocalContext.current
    BoxWithConstraints(
        modifier = modifier
    ) {

        val visibleImages = remember {
            derivedStateOf {
                max(
                    a = 0 ,
                    b = this.maxWidth.div(10.dp + imageSize).toInt()
                        .minus(2) // total width / size+ padding
                )
            }
        }
        val remainingImages = remember {
            derivedStateOf {
                galleryState.images.size - visibleImages.value
            }
        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(imageSize)
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .clickable {
                        onAdd()
                        multiplePhotoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    } ,
                contentAlignment = Alignment.Center
            )
            {
                Text(text = "+" , fontSize = MaterialTheme.typography.titleLarge.fontSize,color =MaterialTheme.colorScheme.onSecondary)
            }
            Spacer(modifier = Modifier.width(10.dp))
            galleryState.images.take(visibleImages.value).forEach { image ->
                AsyncImage(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape)
                        .clickable { onImageClick(image) },
                    model = ImageRequest.Builder(context)
                        .data(image.image)
                        .crossfade(true)
                        .build() ,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Gallery Images"
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            if (remainingImages.value > 0) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                )
                {
                    Text(text = "+${remainingImages.value}")
                }
            }
        }



    }


}