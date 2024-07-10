package com.adityabhatt.moodb

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.adityabhatt.moodb.data.EditMoodViewModel
import com.adityabhatt.moodb.data.MoodDatabaseViewModel
import com.adityabhatt.moodb.data.StateOfScreen
import com.adityabhatt.moodb.ui.EditMoodScreen
import com.adityabhatt.moodb.ui.TodayMoodScreen
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodbTheme {
                val databaseViewModel: MoodDatabaseViewModel = viewModel()
                val navController = rememberNavController()
                MainActivityScreen(navController = navController, databaseViewModel = databaseViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    databaseViewModel: MoodDatabaseViewModel
) {
    var stateOfScreen: StateOfScreen by remember {
        mutableStateOf(StateOfScreen.AddItem)
    }

    val currentScreen = navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Moodb")
                },
                actions = {
                    if (currentScreen.value?.destination?.route == MoodScreens.MoodChange.name && stateOfScreen is StateOfScreen.EditItem) {
                        IconButton(onClick = {
                            EditMoodViewModel.shouldDelete = true
                            navController.popBackStack()
                        }) {
                            Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            when (currentScreen.value?.destination?.route) {
                MoodScreens.MoodStart.name -> {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Add Mood") },
                        icon = { Icon(Icons.Filled.Add, contentDescription = "add mood") },
                        onClick = {
                            stateOfScreen = StateOfScreen.AddItem
                            navController.navigate(MoodScreens.MoodChange.name)
                        }
                    )
                }
                MoodScreens.MoodChange.name -> {
                    ExtendedFloatingActionButton(
                        text = { Text(text = "Save Mood") },
                        icon = { Icon(Icons.Filled.Save, contentDescription = "save mood") },
                        onClick = {
                            EditMoodViewModel.shouldSave = true
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            modifier = modifier.padding(it),
            navController = navController,
            startDestination = MoodScreens.MoodStart.name,
        ) {
            composable(MoodScreens.MoodStart.name) {
                TodayMoodScreen(databaseViewModel) { moodData ->
                    stateOfScreen = StateOfScreen.EditItem(moodData)
                    navController.navigate(MoodScreens.MoodChange.name)
                }
            }
            composable(MoodScreens.MoodChange.name) {
                EditMoodScreen(databaseViewModel = databaseViewModel, stateOfScreen = stateOfScreen)
            }
        }
    }
}
enum class MoodScreens {
    MoodStart,
    MoodChange,
}

/*@Preview(
    showBackground = true,
    name = "Light Mode",
    apiLevel = 33
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode",
    apiLevel = 33
)*/
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
        }
    }
}

