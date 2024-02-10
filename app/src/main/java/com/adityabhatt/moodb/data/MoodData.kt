package com.adityabhatt.moodb.data

import java.time.LocalDateTime


data class MoodData(
    val id: Int,
    val dateTime: LocalDateTime,
    val causeList: List<String>,
    val mood: Mood
)

enum class Mood(val moodString: String) {
    GOOD_MOOD("Good Mood"),
    BAD_MOOD("Bad Mood")
}
