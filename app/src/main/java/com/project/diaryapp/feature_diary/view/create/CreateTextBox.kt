package com.project.diaryapp.feature_diary.view.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.diaryapp.feature_diary.domain.model.Diary

@Composable
fun CreateTextBox(
    modifier: Modifier = Modifier ,
    focusManager: FocusManager,
    diaryTitle: String ,
    onTitleChange: (String) -> Unit ,
    diaryDescription: String ,
    onDescriptionChange: (String) -> Unit ,
) {
    val scrollState = rememberScrollState()

    var title by remember { mutableStateOf(diaryTitle) }
    var body by remember { mutableStateOf(diaryDescription) }

    val focusRequester = remember{FocusRequester()}

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp) ,
    ) {
        //TITLE
        TextField(
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester) ,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ) ,
            keyboardActions = KeyboardActions(
                onDone = {focusManager.clearFocus()}
            ) ,
            value = title ,
            onValueChange = {
                title = it
                onTitleChange(title)
            } ,
            singleLine = true ,
            placeholder = {
                Text(
                    modifier = Modifier.fillMaxWidth() ,
                    text = "Title" ,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground ,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize ,
                        textAlign = TextAlign.Center ,
                        fontWeight = FontWeight.Medium
                    )
                )
            } ,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground ,
                fontSize = MaterialTheme.typography.titleLarge.fontSize ,
                textAlign = TextAlign.Center ,
                fontWeight = FontWeight.Medium
            ) ,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.background ,
                focusedIndicatorColor = MaterialTheme.colorScheme.onBackground ,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary
            )
        )
        //BODY
        TextField(
            modifier = Modifier.fillMaxWidth() ,
            value = body ,
            onValueChange = {
                body = it
                onDescriptionChange(body)
            } ,
            placeholder = {
                Text(text = "So, what's up?" , color = MaterialTheme.colorScheme.onBackground)
            } ,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground ,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            ) ,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colorScheme.background ,
                focusedIndicatorColor = MaterialTheme.colorScheme.background ,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background
            )
        )
    }

}