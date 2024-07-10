package com.adityabhatt.moodb

import android.app.Application
import androidx.room.Room
import com.adityabhatt.moodb.data.MoodDataDatabase

class MoodbApp: Application() {
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            MoodDataDatabase::class.java, "mood_database.db"
        ).build()
    }
}