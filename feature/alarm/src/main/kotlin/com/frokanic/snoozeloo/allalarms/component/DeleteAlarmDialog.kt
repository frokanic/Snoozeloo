package com.frokanic.snoozeloo.allalarms.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAlarmDialog(
    modifier: Modifier = Modifier,
    onCancel: () -> Unit,
    onCompleteDeletion: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onCancel() },
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
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Delete Alarm",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Faded_Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Are you sure you want to delete this alarm?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Faded_Black,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { onCancel() }
                    ) {
                        Text(
                            text = "Cancel",
                            color = Snoozeloo_Blue
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = { onCompleteDeletion() }
                    ) {
                        Text(
                            text = "Delete",
                            color = Snoozeloo_Blue
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DeleteAlarmDialogPreview() {
    DeleteAlarmDialog(
        onCancel = {  },
        onCompleteDeletion = {  }
    )
}