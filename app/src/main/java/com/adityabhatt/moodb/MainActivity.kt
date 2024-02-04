package com.adityabhatt.moodb

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.adityabhatt.moodb.ui.EditMood
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MoodbTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
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
    val date = LocalDate.now()
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
            EditMood(date = date)
        }
    }
}

