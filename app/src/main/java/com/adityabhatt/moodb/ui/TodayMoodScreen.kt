package com.adityabhatt.moodb.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adityabhatt.moodb.data.Mood
import com.adityabhatt.moodb.data.MoodData
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.ResolverStyle
import java.util.Locale
import androidx.compose.ui.text.TextStyle

@Composable
fun TodayMoodScreen(
    listOfMoodData: List<MoodData>
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(listOfMoodData) {
            MoodItem(moodData = it)
        }
    }
}

@Composable
fun MoodItem(
    modifier: Modifier = Modifier,
    moodData: MoodData
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = modifier
                .weight(0.14f)
                .aspectRatio(1f),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Text(
                text = DateTimeFormatter.ofPattern("E").format(moodData.dateTime),
                fontSize = 8.sp,
                style = TextStyle.Default.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp, bottom = 0.dp)
                    .fillMaxWidth()
            )
            Text(
                text = moodData.dateTime.dayOfMonth.toString(),
                fontSize = 16.sp,
                style = TextStyle.Default.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                textAlign = TextAlign.Center,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 8.dp)
                    .padding(bottom = 8.dp, top = 0.dp)
                    .fillMaxWidth()
            )
        }
        Spacer(modifier = modifier.size(8.dp))
        FilledTonalButton(
            modifier = modifier.weight(1f),
            onClick = { /*TODO*/ },
            shape = ShapeDefaults.Small,
        ) {
            Text(text = moodData.mood.moodString, modifier = modifier.fillMaxWidth())
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun MoodItemPreview() {
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    val mood = Mood.GOOD_MOOD
    val dateTime = LocalDateTime.now()
    val moodData = MoodData(0, dateTime, listOfCauses, mood)
    MoodbTheme {
        MoodItem(moodData = moodData)
    }
}

@Preview(showBackground = true)
@Composable
fun MoodListPreview() {
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    val goodMood = Mood.GOOD_MOOD
    val badMood = Mood.BAD_MOOD
    val dateTime1 = LocalDateTime.now()
    val dateTime2 = LocalDateTime.now().plusDays(10)
    val moodData1 = MoodData(0, dateTime1, listOfCauses, goodMood)
    val moodData2 = MoodData(1, dateTime2, listOfCauses, badMood)
    val listOfMoodData = listOf(moodData1, moodData2)
    MoodbTheme {
        TodayMoodScreen(listOfMoodData = listOfMoodData)
    }
}