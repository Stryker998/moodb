package com.adityabhatt.moodb.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDataDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertMoodData(moodData: MoodData)

    @Delete
    suspend fun deleteMoodData(moodData: MoodData)

    @Update
    suspend fun updateMoodData(moodData: MoodData)

    @Query("SELECT * FROM mooddata")
    fun getAll(): Flow<List<MoodData>>

    @Query("SELECT * FROM mooddata ORDER BY date")
    fun getAllSorted(): Flow<List<MoodData>>

    @Query("SELECT * FROM mooddata WHERE id = :id")
    fun getById(id: Int): Flow<MoodData>
}