package com.project.diaryapp.feature_diary.view.one_tap

data class SignInResult(
    val data : UserData?,
    val error : String?
)

data class UserData(
    val userID : String,
    val userName : String?,
    val userPfpUrl : String?
)