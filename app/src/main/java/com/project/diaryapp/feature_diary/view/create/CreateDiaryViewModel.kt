package com.project.diaryapp.feature_diary.view.create

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import com.project.diaryapp.feature_diary.data.repository.UploadImageImpl
import com.project.diaryapp.feature_diary.data.source.FirebaseRepositoryImpl
import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.domain.model.GalleryImage
import com.project.diaryapp.feature_diary.domain.model.GalleryState
import com.project.diaryapp.feature_diary.domain.model.ImageToUpload
import com.project.diaryapp.feature_diary.util.RequestState
import com.project.diaryapp.feature_diary.view.one_tap.GoogleAuthClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateDiaryViewModel @Inject constructor(

    private val googleAuthClient: GoogleAuthClient ,
    private val firestore: FirebaseFirestore ,
    private val storage: FirebaseStorage ,
    private val uploadImage: UploadImageImpl ,
    private val context: Context

) : ViewModel() {

    //firebase
    private val storageReference = storage.getReference("images")
    private val firebaseRepository = FirebaseRepositoryImpl(firestore)


    private val user = googleAuthClient.getCurrentUser()

    //states ->
    private val _uiDiaryState = mutableStateOf(DiaryState())
    val uiDiaryState: State<DiaryState> get() = _uiDiaryState

    private val _galleryState = mutableStateOf(GalleryState())
    val galleryState: State<GalleryState> get() = _galleryState

    private val downloadUrlList = mutableListOf<String>()

    //add diary
    fun addDiaryToDatabase(diary: Diary , onDone: () -> Unit) {

        /*
        THE PROGRESS BAR IS A JUGAAD HANDLED USING THE STATE VAR IN NAVIGATION
         */

        uploadImagesToFirebase(
            onDone = {
                onDone()
                //println("DONEEE!!!")
                viewModelScope.launch {

                    firebaseRepository.addDiaryToDatabase(
                        user!!.userID ,
                        diary.copy(images = downloadUrlList) ,
                        onSuccess = {
                            Toast.makeText(context , "Diary Added" , Toast.LENGTH_SHORT).show()
                            downloadUrlList.clear()
                            resetState()
                        } ,
                        onError = {
                            Toast.makeText(context , it , Toast.LENGTH_SHORT).show()
                            downloadUrlList.clear()
                        }
                    )
                }
            }
        )

    }

    private fun uploadImagesToFirebase(onDone: () -> Unit) {

        if (_galleryState.value.images.isEmpty()) {
            onDone()
            return
        }

        _galleryState.value.images.onEach { galleryImage ->
            storageReference.child(galleryImage.remoteUrl).putFile(galleryImage.image)
                .addOnSuccessListener { snapshot ->
                    runBlocking {
                        snapshot.storage.downloadUrl.addOnSuccessListener { uri ->

                            println("${downloadUrlList.size} and ${_galleryState.value.images.size}")
                            addEntry(uri.toString())
                            if (downloadUrlList.size == _galleryState.value.images.size) {
                                onDone()
                            }
                        }
                    }
                }.addOnProgressListener { snapshot ->
                    val sessionUri = snapshot.uploadSessionUri
                    /*DOC
                    if the session uri is not null then it means that our upload failed and we need to save that session uri to
                    retry in the future
                     */
                    if (sessionUri != null) {
                        viewModelScope.launch(Dispatchers.IO) {
                            uploadImage.addImageToUpload(
                                ImageToUpload(
                                    localPath = galleryImage.remoteUrl ,
                                    imageUri = galleryImage.image.toString() ,
                                    sessionUri = sessionUri.toString()
                                )
                            )
                        }
                    }
                }
        }

    }

    private fun addEntry(url: String) {

        println(url)
        downloadUrlList.add(url)
    }

    fun retryImageUpload(imageToUpload: ImageToUpload,onDone: () -> Unit){
        storageReference.child(imageToUpload.localPath).putFile(
            imageToUpload.imageUri.toUri() ,
            storageMetadata { },
            imageToUpload.sessionUri.toUri()
        ).addOnSuccessListener {
            onDone()
        }
    }
    //update diary
    fun updateDiary(diary: Diary) {
        viewModelScope.launch {
            firebaseRepository.updateDiaryInDatabase(
                user!!.userID ,
                diary ,
                onSuccess = {
                    Toast.makeText(context , "Diary Updated" , Toast.LENGTH_SHORT).show()
                } ,
                onError = {
                    Toast.makeText(context , it , Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    //getByID
    private fun getDiaryById(diaryID: String) {
        viewModelScope.launch {
            _uiDiaryState.value = _uiDiaryState.value.copy(
                loading = RequestState.Loading
            )
            firestore.collection("diaries").document(user!!.userID).collection("userDiaries")
                .document(diaryID)
                .get()
                .addOnSuccessListener {
                    val diary = it.toObject(Diary::class.java)

                    setCurrentDiary(diary!!)
                    setTitle(diary.title)
                    setDescription(diary.description)
                    setMood(diary.mood)
                    _uiDiaryState.value = _uiDiaryState.value.copy(
                        loading = RequestState.Idle
                    )
                }.addOnFailureListener {

                }

            /*
            database.getReference("diaries").child(user!!.userID).child(diaryID)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val diary = snapshot.getValue(Diary::class.java)
                        setCurrentDiary(diary!!)
                        setTitle(diary.title)
                        setDescription(diary.description)
                        setMood(diary.mood)
                        _uiDiaryState.value = _uiDiaryState.value.copy(
                            loading = RequestState.Idle
                        )
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })*/

        }

    }


    //add images
    fun addImageToState(image: Uri , type: String) {
        val remoteUrl = "${user!!.userID}${UUID.randomUUID()}.$type"
        _galleryState.value.images.add(
            GalleryImage(
                image = image ,
                remoteUrl = remoteUrl
            )
        )
        println("image added ${_galleryState.value.images.size}")
    }

    //SETTERS
    private fun setCurrentDiary(diary: Diary) {
        _uiDiaryState.value = _uiDiaryState.value.copy(
            currentDiary = diary
        )
    }

    fun setMood(mood: String) {
        _uiDiaryState.value = _uiDiaryState.value.copy(
            mood = mood
        )
        println("the mood has been set to $mood")
    }

    fun setTitle(title: String) {
        _uiDiaryState.value = _uiDiaryState.value.copy(
            title = title
        )
    }

    fun setDescription(desc: String) {
        _uiDiaryState.value = _uiDiaryState.value.copy(
            description = desc
        )
    }

    fun setId(id: String) {
        _uiDiaryState.value = _uiDiaryState.value.copy(
            currentDiaryId = id
        )
        getDiaryById(id)
    }

    fun resetState() {
        println("RESET STATE")
        _uiDiaryState.value = DiaryState()
        _galleryState.value = GalleryState()
        downloadUrlList.clear()
    }


}