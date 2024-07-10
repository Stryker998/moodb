package com.adityabhatt.moodb.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime
    = Clock.System.now().toLocalDateTime(timeZone)

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate
    = Clock.System.now().toLocalDateTime(timeZone).date

fun LocalDate.toEpochLong(): Long {
    val localDateTime = LocalDateTime(this, LocalTime(12, 0))
    return localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
}

fun LocalDate.getDate() = if (this.dayOfMonth < 10) {
    "0${this.dayOfMonth}"
} else {
    "${this.dayOfMonth}"
}

fun LocalDate.getFullDate(): String {
    val date = if (this.dayOfMonth < 10) {
        "0${this.dayOfMonth}"
    } else {
        "${this.dayOfMonth}"
    }
    val month = this.month.name.lowercase()
        .replaceFirstChar { it.titlecase() }
    val year = this.year
    return "$date $month $year"
}