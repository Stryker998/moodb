package com.adityabhatt.moodb.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.adityabhatt.moodb.MoodbApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MoodDatabaseViewModel(application: Application): AndroidViewModel(application) {
    private val db = (application as MoodbApp).database

    fun addMood(moodData: MoodData) {
        viewModelScope.launch(Dispatchers.IO) {
            db.moodDataDao().insertMoodData(moodData)
        }
    }
    fun updateMood(moodData: MoodData) {
        viewModelScope.launch(Dispatchers.IO) {
            db.moodDataDao().updateMoodData(moodData)
        }
    }
    fun deleteMood(moodData: MoodData) {
        viewModelScope.launch(Dispatchers.IO) {
            db.moodDataDao().deleteMoodData(moodData)
        }
    }

    fun getAll(): Flow<List<MoodData>> = db.moodDataDao().getAll()

    fun getAllSorted(): Flow<List<MoodData>> = db.moodDataDao().getAllSorted()

    fun getById(id: Int): Flow<MoodData> = db.moodDataDao().getById(id)

}