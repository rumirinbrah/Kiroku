package com.project.diaryapp.feature_diary.view.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.diaryapp.feature_diary.domain.model.Mood
import com.project.diaryapp.feature_diary.util.toFormattedDate
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePageTopBar(
    date : Date = Date(),
    moodName: () ->String ,
    newDiary: Boolean ,
    navigateBack: () -> Unit ,
    onDelete: () -> Unit
) {
    val formattedDate = date.toFormattedDate()
    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(3.dp , spotColor = MaterialTheme.colorScheme.onBackground) ,
        title = {
            Column(
                Modifier.wrapContentSize() ,
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = moodName() ,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold ,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize
                    )
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = formattedDate ,
                    style = TextStyle(
                        fontWeight = FontWeight.Light
                    )
                )
            }
        } ,
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack ,
                    modifier = Modifier ,
                    contentDescription = "go back" ,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        } ,
        actions = {
            IconButton(
                onClick = {

                }
            ) {
                Icon(imageVector = Icons.Default.DateRange , contentDescription = "Select date")
            }
            if (!newDiary) {
                DeleteDropDown {
                    onDelete()
                }
            }
        }
    )
}

@Composable
fun DeleteDropDown(
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    DropdownMenu(
        expanded = expanded ,
        onDismissRequest = { expanded = false }
    ) {

            DropdownMenuItem(
                modifier = Modifier.wrapContentWidth(),
                text = { Text(text = "Delete", modifier = Modifier) } ,
                onClick = {
                    onDelete()
                } ,
            )

    }
    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert ,
            contentDescription = "view options to delete diary"
        )
    }

}



