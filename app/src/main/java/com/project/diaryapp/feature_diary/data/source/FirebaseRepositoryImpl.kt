package com.project.diaryapp.feature_diary.data.source

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.domain.repository.FirebaseRepository
import com.project.diaryapp.feature_diary.util.RequestState
import com.project.diaryapp.feature_diary.util.toLocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

typealias Diaries = Map<LocalDate , List<Diary>>

class FirebaseRepositoryImpl(
    private val firestore: FirebaseFirestore
) : FirebaseRepository {

    private val diaryReference = firestore.collection("diaries")

    override suspend fun getDiaryById(userID: String , diaryID: String): Flow<RequestState<Diary>> =
        flow {
            emit(RequestState.Loading)
            var diary = Diary()
            val result = diaryReference.document(userID).collection("userDiaries").document(diaryID)
                    .get().await()

            diary = result.toObject(Diary::class.java)!!

            emit(RequestState.Success(diary))
        }.catch { error ->
            emit(RequestState.Error(error))
        }


    override suspend fun addDiaryToDatabase(
        userID: String ,
        temp: Diary ,
        onSuccess: () -> Unit ,
        onError: (String) -> Unit
    ) {
        val id = diaryReference.document(userID).collection("userDiaries").document().id
        val diary = temp.copy(id = id)
        diaryReference.document(userID).collection("userDiaries").document(id).set(diary)
            .addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener {
                onError(it.localizedMessage ?: "Error")
            }


    }

    override suspend fun updateDiaryInDatabase(
        userID: String ,
        diary: Diary ,
        onSuccess: () -> Unit ,
        onError: (String) -> Unit
    ) {
        diaryReference.document(userID).collection("userDiaries").document(diary.id).set(diary)
            .addOnFailureListener {
                onError(it.localizedMessage ?: "An unknown error occurred")
            }.addOnSuccessListener {
                onSuccess()
            }
    }

    //status - Whatever the fuk we're doing clearly aint working..the first line is getting called and then it gets into some sort of a
    // halt state..smh gotta figure this outt
    override fun getDiaries(
        userID: String,
        getLastDoc :(DocumentSnapshot) ->Unit
    ): Flow<RequestState<Diaries>> = flow {
        emit(RequestState.Loading)

        val diaryList = mutableListOf<Diary>()
                                                                            //.orderBy("date")  **limit(5)
        val result = diaryReference.document(userID).collection("userDiaries").orderBy("date").get().await()
        getLastDoc(result.documents[result.documents.size-1])
        for (i in result.documents) {
            val diary = i.toObject(Diary::class.java)
            diaryList.add(diary!!)
        }

        //delay(4000)
        val groupedList = diaryList.groupBy {
            it.date.toInstant().toLocalDate()
        }
        println("size is ${groupedList.size}")
        emit(RequestState.Success(groupedList))


    }
    suspend fun temp(userID: String){
        val diaryList = mutableListOf<Diary>()

        val resultList = diaryReference.document(userID).collection("userDiaries").orderBy("date").get().await()
        val lastDoc = resultList.documents[resultList.documents.size-1]
        for (i in resultList.documents) {
            val diary = i.toObject(Diary::class.java)
            diaryList.add(diary!!)
        }


        val groupedList = diaryList.groupBy {
            it.date.toInstant().toLocalDate()
        }
    }


    suspend fun refreshDiaries(snapshot: DocumentSnapshot,userID: String): Flow<RequestState<Diaries>>
    = flow{
        println("REPO Refreshing...")
        val diaryList = mutableListOf<Diary>()
        val resultList = diaryReference.document(userID).collection("userDiaries").orderBy("date").startAfter(snapshot).get().await()

        for (i in resultList.documents) {
            val diary = i.toObject(Diary::class.java)
            diaryList.add(diary!!)
        }
        val groupedList = diaryList.groupBy {
            it.date.toInstant().toLocalDate()
        }
        emit(RequestState.Success(groupedList))

    }

    fun flowTester() :Flow<String> = flow {
        emit("First emit")
        kotlinx.coroutines.delay(1000)
        emit("Second emit")
    }

}





