package com.project.diaryapp.feature_diary.view.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import kotlin.math.max

@Composable
fun GalleryRow(
    modifier: Modifier = Modifier ,
    images: List<String> ,
    imageSize: Dp = 50.dp ,
    shape: Shape = Shapes().small
) {
    val context = LocalContext.current
    BoxWithConstraints(
        modifier = modifier
    ) {

        val visibleImages = remember {
            derivedStateOf {
                max(
                    a = 0 ,
                    b = this.maxWidth.div(10.dp + imageSize).toInt()
                        .minus(1) // total width / size+ padding
                )
            }
        }
        val remainingImages = remember {
            derivedStateOf {
                images.size - visibleImages.value
            }
        }

        Row {
            images.take(visibleImages.value).forEach { image ->
                AsyncImage(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape) ,
                    model = ImageRequest.Builder(context)
                        .data(image)
                        .crossfade(true)
                        .build() ,
                    contentDescription = "Gallery Images",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
            }
            if (remainingImages.value > 0) {
                Box(
                    modifier = Modifier
                        .size(imageSize)
                        .clip(shape) ,
                    contentAlignment = Alignment.Center
                )
                {
                    Text(text = "+${remainingImages.value}")
                }
            }

        }



    }

}


