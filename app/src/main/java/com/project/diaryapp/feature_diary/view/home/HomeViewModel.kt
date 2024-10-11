package com.project.diaryapp.feature_diary.view.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.project.diaryapp.feature_diary.data.source.Diaries
import com.project.diaryapp.feature_diary.data.source.FirebaseRepositoryImpl
import com.project.diaryapp.feature_diary.util.RequestState
import com.project.diaryapp.feature_diary.view.one_tap.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

    private val googleAuthClient: GoogleAuthClient ,
    private val firestore: FirebaseFirestore ,
    private val storage: FirebaseStorage

) : ViewModel() {

    private val firebaseRepository = FirebaseRepositoryImpl(firestore)
    private val user = googleAuthClient.getCurrentUser()

    private val _diaries = MutableStateFlow<RequestState<Diaries>>(RequestState.Loading)
    val diaries = _diaries.asStateFlow()

    private var lastDoc = mutableStateOf<DocumentSnapshot?>(null)


    init {
        getDiaries()
        //flowTester()
    }

    private fun getDiaries() {
        viewModelScope.launch {
            try {
                firebaseRepository.getDiaries(user!!.userID, getLastDoc = {lastDoc.value=it}).collect {
                    when (it) {
                        is RequestState.Error -> TODO()
                        RequestState.Idle -> {
                            _diaries.value = RequestState.Idle
                            println("Idle was emitted")
                        }

                        RequestState.Loading -> {
                            println("Loading was emitted")
                        }

                        is RequestState.Success -> {
                            _diaries.value = it
                            println("Success was emitted")
                        }
                    }
                }
            } catch (e: Exception) {
                _diaries.value = RequestState.Error(e)
            }

        }


    }

    fun flowTester() {


    }


    fun refreshDiaries() {

        viewModelScope.launch {
            firebaseRepository.refreshDiaries(lastDoc.value!!,user!!.userID).collect{newState->
                when(newState){
                    is RequestState.Error -> TODO()
                    RequestState.Idle -> TODO()
                    RequestState.Loading -> TODO()
                    is RequestState.Success -> {
                        _diaries.update {
                            val idk = (_diaries.value as RequestState.Success<Diaries>).data
                            println("idk ka length hai ${idk.size}")
                            val currentDiaries = newState.data + idk

                            RequestState.Success(currentDiaries)
                        }
                    }
                }

            }
        }

    }

    fun temp() {
        liveData(Dispatchers.IO) {
            emit(RequestState.Loading)
            println("loading.....")
            try {

                val result = firebaseRepository.getDiaries(user!!.userID, getLastDoc = {})
                println("success.....")
                emit(result)
            } catch (e: Exception) {
                e.printStackTrace()
                println("errur.....")
                emit(RequestState.Error(e))
            }
        }


    }
}