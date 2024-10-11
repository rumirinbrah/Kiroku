package com.project.diaryapp.feature_diary.view.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.project.diaryapp.R
import com.project.diaryapp.feature_diary.data.source.Diaries
import com.project.diaryapp.feature_diary.util.RequestState
import com.project.diaryapp.feature_diary.util.Screen
import com.project.diaryapp.feature_diary.view.one_tap.UserData
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    userData: UserData? ,
    navController: NavController ,
    diaries: RequestState<Diaries>? ,
    onSignOut: () -> Unit ,
    refresh: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection) ,
        backgroundColor = MaterialTheme.colorScheme.background ,
        scaffoldState = scaffoldState ,
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior ,
                onHomeClicked = {
                    scope.launch {
                        scaffoldState.drawerState.open()

                    }
                }
            )
        } ,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.CreateScreen.route + "/null")
                }
            ) {
                Icon(imageVector = Icons.Default.Edit , contentDescription = "Add/Edit Diary")
            }
        } ,
        drawerContent = {
            NavigationDrawer(
                userData = userData
            ) {
                onSignOut()
            }
        } ,
        drawerBackgroundColor = MaterialTheme.colorScheme.background
    ) {

        HomeContent(
            modifier = Modifier.padding(it) ,
            diaries = diaries ,
            onClick = { id ->
                navController.navigate(Screen.CreateScreen.route + "/$id")
            } ,
            refresh = { refresh() }
        )

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier ,
    diaries: RequestState<Diaries>? ,
    onClick: (String) -> Unit ,
    refresh: () -> Unit
) {


    Column {

        if (diaries == null) {
            Column(
                modifier.fillMaxSize() ,
                horizontalAlignment = Alignment.CenterHorizontally ,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your Diaries will appear here" ,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize ,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                )


            }
        } else {
            when (diaries) {
                is RequestState.Error -> {
                    Text(text = (diaries as RequestState.Error).error.message ?: "")

                }

                RequestState.Idle -> {

                }

                RequestState.Loading -> {
                    Row(
                        Modifier.fillMaxWidth() ,
                        horizontalArrangement = Arrangement.Center
                    ) {

                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                    }
                }

                is RequestState.Success -> {
                    val currentDiaries = (diaries as RequestState.Success<Diaries>).data
                    LazyColumn {

                        currentDiaries.forEach { (localDate , diaryList) ->
                            stickyHeader(localDate) {
                                DateHeader(localDate = localDate)
                            }
                            items(
                                items = diaryList ,
                                key = { it.id }
                            ) { diary ->
                                DiaryItem(
                                    diary = diary ,
                                    onClick = {
                                        onClick(it)
                                    }
                                )
                            }
                        }
                        item {
                            Row(
                                Modifier.fillMaxWidth() ,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        //refresh()
                                    }) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh ,
                                        contentDescription = null
                                    )
                                }
                            }


                        }

                    }
                }

                else -> {

                }
            }
        }
    }
}


@Composable
fun NavigationDrawer(
    userData: UserData? ,
    onSignOut: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(10.dp))
    //Image(painter = painterResource(id = R.drawable.logo_svg) , contentDescription =null )
    Column(
        Modifier.fillMaxWidth() ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userData?.userPfpUrl != null) {
            AsyncImage(
                model = userData.userPfpUrl ,
                contentDescription = null ,
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = userData.userName!! , fontSize = 18.sp , fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary , RoundedCornerShape(45))
                .padding(vertical = 8.dp , horizontal = 20.dp)
                .clickable {
                    showDialog = true
                } ,
            verticalAlignment = Alignment.CenterVertically ,
            //horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.google_logo) ,
                contentDescription = null ,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
            Text(
                text = "Sign Out" ,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            backgroundColor = MaterialTheme.colorScheme.tertiary ,
            contentColor = MaterialTheme.colorScheme.onTertiary ,
            text = {
                Text(
                    text = "Are you sure you want to sign out?" ,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            } ,
            onDismissRequest = { showDialog = false } ,
            buttons = {
                Row(
                    Modifier.fillMaxWidth() ,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showDialog = false
                            onSignOut()
                        }
                    ) {
                        Text(text = "Yes")
                    }
                    Spacer(Modifier.width(5.dp))
                    Button(onClick = { showDialog = false }) {
                        Text(text = "No")
                    }
                }
            }
        )
    }
}


