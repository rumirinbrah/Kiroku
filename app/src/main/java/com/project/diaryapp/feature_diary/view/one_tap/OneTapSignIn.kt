package com.project.diaryapp.feature_diary.view.one_tap

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.project.diaryapp.R

@Composable
fun OneTapSignIn(
    user : UserData?,
    signInState: SignInState,
    onSignIn:()->Unit
) {

    val context = LocalContext.current
    var isLoading by remember{ mutableStateOf(false) }


    LaunchedEffect(signInState.error ){
        signInState.error?.let {error->
            isLoading = false
            Toast.makeText(context , error , Toast.LENGTH_SHORT).show()
        }
    }


    Box(
        modifier = Modifier
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if(user!=null){
                //Toast.makeText(context , user.userID , Toast.LENGTH_SHORT).show()
            }
            Image(
                modifier= Modifier
                    .size(90.dp)
                    .padding(10.dp),
                painter = painterResource(id = R.drawable.google_logo) ,
                contentDescription = null
            )
            Text(text = "Welcome Back", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Please sign in to continue", fontSize = 15.sp, fontWeight = FontWeight.Light)

            Spacer(modifier = Modifier.height(20.dp))

            GoogleButton(
                isLoading = isLoading ,
                modifier = Modifier
            ) {
                onSignIn()
                //for prog bar
                isLoading = true
            }

        }

    }
}



@Composable
fun GoogleButton(
    modifier: Modifier ,
    primaryText : String="Sign in with Google" ,
    secondaryText : String="Signing in,please wait..." ,
    isLoading : Boolean = false ,
    shape : Shape = Shapes().extraSmall ,
    borderStrokeWidth : Dp = 2.dp ,
    borderColor : Color = MaterialTheme.colorScheme.surfaceVariant ,
    progressIndicatorColor : Color = MaterialTheme.colorScheme.primary ,
    backgroundColor : Color = MaterialTheme.colorScheme.onSecondary ,
    onClick : ()-> Unit
) {

    var buttonText by remember{ mutableStateOf(primaryText) }

    LaunchedEffect(isLoading ){
        buttonText = if(isLoading) secondaryText else primaryText
    }

    Surface(
        modifier = modifier.clickable(
            enabled = !isLoading
        ) {
            onClick()
        } ,
        shape = shape,
        border = BorderStroke(borderStrokeWidth, color = borderColor) ,
        color = backgroundColor
    ) {

        Row(
            Modifier
                .wrapContentWidth()
                .padding(10.dp)
                .animateContentSize(tween(500)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.google_logo) ,
                contentDescription =null
            )
            Spacer(Modifier.width(5.dp))

            Text(text = buttonText)

            Spacer(Modifier.width(5.dp))
            if(isLoading){
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(30.dp),
                    color = progressIndicatorColor,
                )
            }

        }
    }



}




