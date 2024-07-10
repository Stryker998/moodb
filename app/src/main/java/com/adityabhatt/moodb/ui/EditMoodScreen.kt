package com.adityabhatt.moodb.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material.icons.outlined.MoodBad
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.adityabhatt.moodb.data.EditMoodViewModel
import com.adityabhatt.moodb.data.EditMoodViewModel.DialogState
import com.adityabhatt.moodb.data.Mood
import com.adityabhatt.moodb.data.MoodDatabaseViewModel
import com.adityabhatt.moodb.data.StateOfScreen
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import com.adityabhatt.moodb.util.getFullDate
import com.adityabhatt.moodb.util.now
import com.adityabhatt.moodb.util.toEpochLong
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


@Composable
fun EditMoodScreen(
    modifier: Modifier = Modifier,
    databaseViewModel: MoodDatabaseViewModel,
    stateOfScreen: StateOfScreen
) {
    val editMoodViewModel: EditMoodViewModel = viewModel(factory = EditMoodViewModel.Factory(databaseViewModel, stateOfScreen))

    val date = editMoodViewModel.currentDate.observeAsState()
    val mood = editMoodViewModel.mood.observeAsState()
    val causeList = editMoodViewModel.causeList.observeAsState()


    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        EditMood(date = date, mood = mood, moodChange = { editMoodViewModel.changeMood() }, updateDate = { editMoodViewModel.updateDate(it) })
        Spacer(modifier = modifier.size(16.dp))
        CausesList(listOfCauses = causeList, updateList = { editMoodViewModel.updateList(it) })
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMood(
    modifier: Modifier = Modifier,
    date: State<LocalDate?>,
    mood: State<Mood?>,
    moodChange: () -> Unit,
    updateDate: (Long) -> Unit,
) {
    var openDatePickerDialog by remember {
        mutableStateOf(false)
    }
    Column {
        FilledTonalButton(
            onClick = moodChange,
            shape = ShapeDefaults.Small,
            contentPadding = PaddingValues(
                start = 10.dp,
                bottom = 8.dp,
                top = 8.dp,
                end = 24.dp,
            )
        ) {
            when (mood.value!!) {
                Mood.GOOD_MOOD -> Icon(imageVector = Icons.Outlined.Mood, contentDescription = "Good Mood")
                Mood.BAD_MOOD -> Icon(imageVector = Icons.Outlined.MoodBad, contentDescription = "Bad Mood")
            }
            Spacer(modifier = modifier.size(8.dp))
            Text(text = mood.value!!.moodString)
        }
        Spacer(modifier = modifier.size(8.dp))
        FilledTonalButton(
            onClick = {
                openDatePickerDialog = true
            },
            shape = ShapeDefaults.Small,
            contentPadding = PaddingValues(
                start = 10.dp,
                bottom = 8.dp,
                top = 8.dp,
                end = 24.dp,
            )
        ) {
            Icon(imageVector = Icons.Outlined.CalendarToday, contentDescription = "Calendar")
            Spacer(modifier = modifier.size(8.dp))
            Text(text = date.value!!.getFullDate())
        }
    }
    if (openDatePickerDialog) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.value?.toEpochLong())
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = { openDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        updateDate(datePickerState.selectedDateMillis!!)
                        openDatePickerDialog = false
                    },
                    enabled = confirmEnabled
                ) {
                    Text(text = "Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDatePickerDialog = false }) {
                    Text(text = "Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CausesList(
    modifier: Modifier = Modifier,
    listOfCauses: State<List<String>?>,
    updateList: (DialogState) -> Unit
) {

    var openCauseListDialog by remember {
        mutableStateOf(Pair<Boolean, DialogState>(false, DialogState.ADD()))
    }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = ShapeDefaults.Small
            )
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        Text(
            text = "Causes",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
        )
        Spacer(modifier = modifier.size(10.dp))
        listOfCauses.value!!.forEachIndexed { index, string ->
            FilledTonalButton(
                onClick = {
                    openCauseListDialog = Pair(true, DialogState.EDIT(index, string))
                },
                modifier = modifier.fillMaxWidth(),
                shape = ShapeDefaults.Small,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary

                ),
                elevation = ButtonDefaults.elevatedButtonElevation()
            ) {
                Text(string,
                    textAlign = TextAlign.Start,
                    modifier = modifier.fillMaxWidth(),
                    overflow = TextOverflow.Visible,
                    softWrap = true
                )
            }
        }

        TextButton(
            onClick = {
                openCauseListDialog = Pair(true, DialogState.ADD())
            },
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 2.dp,
                top = ButtonDefaults.ContentPadding.calculateTopPadding(),
                end = 16.dp,
                bottom = ButtonDefaults.ContentPadding.calculateBottomPadding()
            ),
            shape = ShapeDefaults.Small,
        ) {
            Icon(Icons.Outlined.Add, "Add")
            Spacer(modifier = modifier.size(4.dp))
            Text("Add cause",
                textAlign = TextAlign.Start,
                modifier = modifier.fillMaxWidth(),
                overflow = TextOverflow.Visible,
                softWrap = true
            )
        }
    }
    if (openCauseListDialog.first) {
        AddCause(
            onDismissRequest = { openCauseListDialog = Pair(false, DialogState.ADD()) },
            onConfirmation = {
                updateList(it)
                openCauseListDialog = Pair(false, DialogState.ADD())
            },
            dialogState = openCauseListDialog.second
        )
    }
}

@Composable
fun AddCause(
    onDismissRequest: () -> Unit,
    onConfirmation: (DialogState) -> Unit,
    dialogState: DialogState
) {
    var inputText by remember {
        mutableStateOf(dialogState.causeString)
    }
    var isError by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
                Text(
                    when (dialogState) {
                        is DialogState.ADD -> { "Add Cause" }
                        is DialogState.DELETE -> { throw Exception() }
                        is DialogState.EDIT -> { "Edit Cause" }
                    }
                )
        },
        text = {
               OutlinedTextField(
                   value = inputText,
                   onValueChange = {
                       if (isError) { isError = false }
                       inputText = it
                   },
                   label = {
                       Text(text = "Cause")
                   },
                   isError = isError,
                   supportingText = {
                       if (isError) {
                           Text(
                               text = "Please enter a cause",
                               color = MaterialTheme.colorScheme.error
                           )
                       }
                   },
                   trailingIcon = {
                       if (isError) {
                           Icon(
                               Icons.Rounded.Error,
                               "Error",
                               tint = MaterialTheme.colorScheme.error
                               )
                       }
                   }
               )
        },
        confirmButton = {
            TextButton(onClick = {
                if (inputText.isBlank()) {
                    isError = true
                } else {
                    val newState = when (dialogState) {
                        is DialogState.ADD -> dialogState.copy(inputText)
                        is DialogState.EDIT -> dialogState.copy(causeString = inputText)
                        is DialogState.DELETE -> { throw Exception() }
                    }
                    onConfirmation(newState)
                }
            }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            if (dialogState is DialogState.EDIT) {
                TextButton(onClick = {
                    onConfirmation(DialogState.DELETE(dialogState.index))
                }) {
                    Text(text = "Delete")
                }
            }
        },
    )
}


@Preview(showBackground = true, apiLevel = 33, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, apiLevel = 33)
@Composable
fun EditMoodPreview() {
    // Testing variables
    val date = remember {
        mutableStateOf(LocalDate.now())
    }
    val listOfCauses = remember {
        mutableStateOf(listOf(
            "Great night sleep",
            "Exercised first thing",
            "Worked on most important task"
        ))
    }
    val mood = remember {
        mutableStateOf(Mood.GOOD_MOOD)
    }
    val changeMood = {
        if (mood.value == Mood.GOOD_MOOD) {
            mood.value = Mood.BAD_MOOD
        } else {
            mood.value = Mood.GOOD_MOOD
        }
    }
    val modifier = Modifier
    MoodbTheme {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            Column(modifier = modifier.padding(horizontal = 16.dp)) {
                EditMood(date = date, mood = mood, moodChange = changeMood, updateDate = {
                    date.value = Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date
                })
                Spacer(modifier = modifier.size(12.dp))
                CausesList(listOfCauses = listOfCauses, updateList = {
                    when (it) {
                        is DialogState.ADD -> {
                            val newList = listOfCauses.value.toMutableList()
                            newList.add(it.causeString)
                            listOfCauses.value = newList
                        }
                        is DialogState.DELETE -> {
                            val newList = listOfCauses.value.toMutableList()
                            newList.removeAt(it.index)
                            listOfCauses.value = newList
                        }
                        is DialogState.EDIT -> {
                            val newList = listOfCauses.value.toMutableList()
                            newList[it.index] = it.causeString
                            listOfCauses.value = newList
                        }
                    }
                })
            }
        }
    }
}