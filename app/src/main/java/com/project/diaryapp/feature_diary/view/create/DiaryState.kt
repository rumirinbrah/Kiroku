package com.project.diaryapp.feature_diary.view.create

import com.project.diaryapp.feature_diary.domain.model.Diary
import com.project.diaryapp.feature_diary.domain.model.Mood
import com.project.diaryapp.feature_diary.util.RequestState

data class DiaryState(
    val loading : RequestState<*> = RequestState.Idle,
    val currentDiary : Diary? = null,
    val currentDiaryId : String? = null,
    val title : String="",
    val description : String="",
    val mood: String = "Happy"
)
