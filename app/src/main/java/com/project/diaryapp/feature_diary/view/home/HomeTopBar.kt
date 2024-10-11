package com.project.diaryapp.feature_diary.view.home

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onHomeClicked:() -> Unit
) {
    androidx.compose.material3.TopAppBar(
        scrollBehavior = scrollBehavior,
        //contentColor = MaterialTheme.colorScheme.onBackground,
        //backgroundColor = MaterialTheme.colorScheme.background,
        title = { Text(text = "Welcome", color = MaterialTheme.colorScheme.onBackground, fontSize =MaterialTheme.typography.titleLarge.fontSize) },
        navigationIcon = {
            IconButton(onClick = { onHomeClicked() }) {
                Icon(
                    imageVector = Icons.Default.Menu ,
                    contentDescription = "Menu" ,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(
                onClick = {

                }
            )
            {
                Icon(
                    imageVector = Icons.Default.DateRange ,
                    contentDescription = "Date Range",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}