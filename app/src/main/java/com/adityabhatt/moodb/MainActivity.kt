package com.adityabhatt.moodb

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adityabhatt.moodb.data.Mood
import com.adityabhatt.moodb.data.MoodData
import com.adityabhatt.moodb.ui.EditMood
import com.adityabhatt.moodb.ui.EditMoodScreen
import com.adityabhatt.moodb.ui.TodayMoodScreen
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDate
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MoodbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = MoodScreens.MoodStart.name,
                    ) {
                        // TODO: Implement navigation
                        composable(MoodScreens.MoodStart.name) {
                        }
                        composable(MoodScreens.MoodChange.name) {
                        }
                        composable(MoodScreens.MoodHistory.name) {
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val listOfMoodData = mutableListOf<MoodData>()
    for (i in 0..10) {
        listOfMoodData.add(
            MoodData(
                id = i,
                dateTime = LocalDateTime.now().plusDays(i.toLong()),
                causeList = listOf(),
                mood = if (i % 2 == 0) Mood.GOOD_MOOD else Mood.BAD_MOOD
            )
        )
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = "Moodb")
            })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add Mood") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "add mood") },
                onClick = { /* TODO: Add mood */ }
            )
        }
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = MoodScreens.MoodStart.name,
        ) {
            // TODO: Implement navigation
            composable(MoodScreens.MoodStart.name) {
                TodayMoodScreen(listOfMoodData = listOfMoodData)
            }
            composable(MoodScreens.MoodChange.name) {
            }
            composable(MoodScreens.MoodHistory.name) {
            }
        }
    }
}

enum class MoodScreens {
    MoodStart,
    MoodEdit,
    MoodChange,
    MoodHistory
}

@Preview(
    showBackground = true,
    name = "Light Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun AppPreview() {
    // Testing variables
    val date = LocalDateTime.now()
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    MoodbTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            MainActivityScreen()
        }
    }
}

