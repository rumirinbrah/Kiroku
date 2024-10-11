package com.project.diaryapp.feature_diary.domain.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.project.diaryapp.feature_diary.data.source.Diaries
import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FirebaseRepository {

    suspend fun getDiaryById(userID : String,diaryID: String) : Flow<RequestState<Diary>>//RequestState<Diary>

    suspend fun addDiaryToDatabase(userID : String, diary: Diary,onSuccess :()->Unit,onError:(String)->Unit)

    suspend fun updateDiaryInDatabase(userID : String, diary: Diary,onSuccess :()->Unit,onError:(String)->Unit)

    fun getDiaries(userID: String,getLastDoc:(DocumentSnapshot)->Unit) : Flow<RequestState<Diaries>>

}