package com.adityabhatt.moodb.data

import java.time.LocalDate


data class MoodData(
    val id: Int,
    val date: LocalDate,
    val causeList: List<String>,
    val mood: Mood
)

enum class Mood {
    GOOD_MOOD, BAD_MOOD
}
