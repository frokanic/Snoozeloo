package com.frokanic.snoozeloo.alarmdetails.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.frokanic.snoozeloo.component.SnoozelooActionButton
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Medium_Grey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameDialog(
    modifier: Modifier = Modifier,
    name: String?,
    onUpdateName: (String) -> Unit,
    onClose: () -> Unit
) {
    var localName by remember { mutableStateOf(name.orEmpty()) }

    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onClose() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Alarm Name",
                    fontSize = 16.sp,
                    color = Faded_Black
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    value = localName,
                    onValueChange = { newValue ->
                        localName = newValue
                    },
                    singleLine = true,
                    label = null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Medium_Grey,
                        unfocusedBorderColor = Medium_Grey
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    SnoozelooActionButton(
                        text = "Save",
                        onClick = {
                            onUpdateName(localName)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun NameDialogPreview() {
    NameDialog(
        name = null,
        onUpdateName = {  },
        onClose = {  },
    )
}

@Preview
@Composable
private fun NameDialogFilledPreview() {
    NameDialog(
        name = "Name Example",
        onUpdateName = {  },
        onClose = {  },
    )
}

@Preview
@Composable
private fun NameDialogOverfilledPreview() {
    NameDialog(
        name = "Name Example That is Filled With not Really meaningfull text but text nontheless",
        onUpdateName = {  },
        onClose = {  },
    )
}