package com.project.diaryapp.feature_diary.view.create

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.project.diaryapp.feature_diary.domain.model.Mood

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MoodSelector(
    modifier: Modifier = Modifier ,
    pagerState: com.google.accompanist.pager.PagerState
) {

    val context = LocalContext.current
    Box(
        modifier.fillMaxWidth() ,
        //contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState ,
            count = Mood.entries.size ,
            modifier = Modifier.wrapContentWidth()
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomCenter) ,
                model = ImageRequest.Builder(context)
                    .data(Mood.entries[it].icon)
                    .crossfade(true)
                    .build(),
                contentDescription = "Mood Image"
            )

        }
    }
}