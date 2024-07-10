package com.adityabhatt.moodb.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [MoodData::class], version = 1, exportSchema = false)
@TypeConverters(MoodConverters::class, LocalDateConverters::class, CauseListConverters::class)
abstract class MoodDataDatabase: RoomDatabase() {
    abstract fun moodDataDao(): MoodDataDao

}