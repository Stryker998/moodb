package com.adityabhatt.moodb.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adityabhatt.moodb.data.EditMoodDataViewModel
import com.adityabhatt.moodb.ui.theme.MoodbTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun EditMood(
    modifier: Modifier = Modifier,
    date: LocalDate,
) {
    val viewModel = EditMoodDataViewModel()
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // TODO: Add mood image
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
    viewModel: EditMoodDataViewModel,
) {
    val listOfCauses = viewModel.causeLists.observeAsState()
    Column {
        listOfCauses.value!!.forEach {
            FilledTonalButton(
                onClick = { /*TODO*/ },
                modifier = modifier.fillMaxWidth(),
                shape = ShapeDefaults.Small
            ) {
                Text(it,
                    textAlign = TextAlign.Start,
                    modifier = modifier.fillMaxWidth(),
                    overflow = TextOverflow.Visible,
                    softWrap = true
                )
            }
        }

        TextButton(
            onClick = {
                      viewModel.updateList("Another one")
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
}



@Preview(showBackground = true)
@Composable
fun EditMoodPreview() {
    // Testing variables
    val date = LocalDate.now()
    val listOfCauses = listOf(
        "Great night sleep",
        "Exercised first thing",
        "Worked on most important task"
    )
    MoodbTheme {
        EditMood(date = date)
    }
}