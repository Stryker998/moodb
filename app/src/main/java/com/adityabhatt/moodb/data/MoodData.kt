package com.adityabhatt.moodb.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.datetime.LocalDate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Entity
data class MoodData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @TypeConverters(LocalDateConverters::class)
    val date: LocalDate,
    @ColumnInfo(name = "cause_list")
    @TypeConverters(CauseListConverters::class)
    val causeList: List<String>,
    @TypeConverters(MoodConverters::class)
    val mood: Mood
)
class LocalDateConverters {
    @TypeConverter
    fun stringToLocalDate(value: String) = LocalDate.parse(value)


    @TypeConverter
    fun localDateTimeToString(value: LocalDate) = value.toString()

}

class CauseListConverters {
    @TypeConverter
    fun listToJson(value: List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun jsonToList(value: String) = Json.decodeFromString<List<String>>(value)
}

class MoodConverters {
    @TypeConverter
    fun nameToMood(value: String) = Mood.valueOf(value)

    @TypeConverter
    fun moodToName(value: Mood) = value.name
}
enum class Mood(val moodString: String) {
    GOOD_MOOD("Good Mood"),
    BAD_MOOD("Bad Mood")
}
