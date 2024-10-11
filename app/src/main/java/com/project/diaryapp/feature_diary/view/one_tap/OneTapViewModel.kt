package com.project.diaryapp.feature_diary.view.one_tap

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class OneTapViewModel @Inject constructor():ViewModel(){

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow() //same as normal approach

    fun onSignInResult(signInResult: SignInResult){
        Log.d(TAG , "onSignInResult:Calleed ")
        _state.update {
            it.copy(
                isSignInSuccessful = signInResult.data != null,
                error = signInResult.error
            )
        }
    }

    fun resetState(){
        _state.update {
            it.copy(
                isSignInSuccessful = false,
                error = null
            )
        }
    }

}