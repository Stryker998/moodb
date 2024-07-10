package com.adityabhatt.moodb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabhatt.moodb.data.Mood
import com.adityabhatt.moodb.data.MoodData
import com.adityabhatt.moodb.data.MoodDatabaseViewModel
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import com.adityabhatt.moodb.util.getDate
import com.adityabhatt.moodb.util.now
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
fun TodayMoodScreen(
    databaseViewModel: MoodDatabaseViewModel,
    goToEdit: (MoodData) -> Unit
) {
    val listOfMoodData = databaseViewModel.getAllSorted().collectAsState(initial = listOf())
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(listOfMoodData.value, key = { it.id }) {
            MoodItem(moodData = it, goToEdit = goToEdit)
        }
    }
}

@Composable
fun MoodItem(
    modifier: Modifier = Modifier,
    moodData: MoodData,
    goToEdit: (MoodData) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = moodData.date.month.name.substring(0..2),
                fontSize = 10.sp,
                style = TextStyle.Default.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = moodData.date.getDate(),
                fontSize = 24.sp,
                style = TextStyle.Default.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = modifier.size(16.dp))
        FilledTonalButton(
            onClick = {
                goToEdit(moodData)
            },
            shape = ShapeDefaults.Small
        ) {
            Text(text = moodData.mood.moodString, modifier = modifier.fillMaxWidth())
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun MoodItemPreview() {
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    val mood = Mood.GOOD_MOOD
    val dateTime = LocalDate.now()
    val moodData = MoodData(0, dateTime, listOfCauses, mood)
    MoodbTheme {
        MoodItem(moodData = moodData, goToEdit = {})
    }
}

//@Preview(showBackground = true, apiLevel = 33)
@Composable
fun TodayMoodScreenPreview() {
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    val goodMood = Mood.GOOD_MOOD
    val badMood = Mood.BAD_MOOD
    val dateTime1 = LocalDate.now()
    val dateTime2 = LocalDate.now().plus(DatePeriod(days = 10))
    val moodData1 = MoodData(0, dateTime1, listOfCauses, goodMood)
    val moodData2 = MoodData(1, dateTime2, listOfCauses, badMood)
    val listOfMoodData = listOf(moodData1, moodData2)
    MoodbTheme {
//        TodayMoodScreen(listOfMoodData = listOfMoodData)
    }
}