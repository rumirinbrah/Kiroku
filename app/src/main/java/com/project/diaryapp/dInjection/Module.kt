package com.project.diaryapp.dInjection

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.project.diaryapp.feature_diary.data.repository.UploadImageImpl
import com.project.diaryapp.feature_diary.data.source.ImageToUploadDatabase
import com.project.diaryapp.feature_diary.util.Constants
import com.project.diaryapp.feature_diary.view.create.CreateDiaryViewModel
import com.project.diaryapp.feature_diary.view.home.HomeViewModel
import com.project.diaryapp.feature_diary.view.one_tap.GoogleAuthClient
import com.project.diaryapp.feature_diary.view.one_tap.OneTapViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun provideOneTapViewModel() : OneTapViewModel{
        return OneTapViewModel()
    }

    @Provides
    @Singleton
    fun provideCreateViewModel(
        @ApplicationContext context: Context ,
        googleAuthClient: GoogleAuthClient ,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage,
        uploadImageImpl: UploadImageImpl
    ) : CreateDiaryViewModel{
        return CreateDiaryViewModel(googleAuthClient,firestore,storage,uploadImageImpl,context)
    }

    @Provides
    @Singleton
    fun provideHomeViewModel(
        googleAuthClient: GoogleAuthClient ,
        firestore: FirebaseFirestore,
        storage: FirebaseStorage
    ) : HomeViewModel{
        return HomeViewModel(googleAuthClient=googleAuthClient, firestore = firestore,storage=storage)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthClient(@ApplicationContext context: Context) : GoogleAuthClient{
        return GoogleAuthClient(
            context ,
            Identity.getSignInClient(context)
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun getFirebaseAuth() : FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun getFirestore() : FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun getStorage() : FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun provideImageToUploadDatabase(@ApplicationContext context: Context):ImageToUploadDatabase{
        return Room.databaseBuilder(
            context,
            ImageToUploadDatabase::class.java,
            Constants.IMAGES_UPLOAD_DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideUploadImageRepository(imageToUploadDatabase: ImageToUploadDatabase) : UploadImageImpl{
        return UploadImageImpl(imageToUploadDatabase.imageToUploadDao)
    }




}