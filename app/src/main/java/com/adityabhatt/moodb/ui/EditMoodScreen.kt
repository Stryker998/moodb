package com.adityabhatt.moodb.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabhatt.moodb.data.EditMoodViewModel
import com.adityabhatt.moodb.data.EditMoodViewModel.DialogState
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun EditMood(
    modifier: Modifier = Modifier,
    date: LocalDate,
) {
    val viewModel = EditMoodViewModel()
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = modifier.size(16.dp))
        Text(text = "Date: ${date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))}")
        Spacer(modifier = modifier.size(16.dp))
        Text(text = "Causes:-")
        Spacer(modifier = modifier.size(8.dp))
        CausesList(viewModel = viewModel)
    }
}

@Composable
fun CausesList(
    modifier: Modifier = Modifier,
    viewModel: EditMoodViewModel,
) {

    var openDialog by rememberSaveable {
        mutableStateOf(Pair<Boolean, DialogState>(false, DialogState.ADD()))
    }

    val listOfCauses = viewModel.causeLists.observeAsState()

    Column {
        listOfCauses.value!!.forEachIndexed { index, string ->
            FilledTonalButton(
                onClick = {
                    openDialog = Pair(true, DialogState.EDIT(index, string))
                },
                modifier = modifier.fillMaxWidth(),
                shape = ShapeDefaults.Small,
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
                      openDialog = Pair(true, DialogState.ADD())
            },
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                start = 2.dp,
                top = ButtonDefaults.ContentPadding.calculateTopPadding(),
                end = 16.dp,
                bottom = ButtonDefaults.ContentPadding.calculateBottomPadding()
            ),
            shape = ShapeDefaults.Small
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
    if (openDialog.first) {
        AddCause(
            onDismissRequest = { openDialog = Pair(false, DialogState.ADD()) },
            onConfirmation = {
                viewModel.updateList(it)
                openDialog = Pair(false, DialogState.ADD())
            },
            dialogState = openDialog.second
        )
    }
}

@Composable
fun AddCause(
    onDismissRequest: () -> Unit,
    onConfirmation: (DialogState) -> Unit,
    dialogState: DialogState
) {
    var inputText by rememberSaveable {
        mutableStateOf(dialogState.causeString)
    }
    var isError by rememberSaveable {
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


@Preview(showBackground = true)
@Composable
fun EditMoodPreview() {
    // Testing variables
    val date = LocalDate.now()
    MoodbTheme {
        EditMood(date = date)
    }
}