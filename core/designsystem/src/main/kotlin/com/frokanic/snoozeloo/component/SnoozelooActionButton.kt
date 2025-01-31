package com.frokanic.snoozeloo.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frokanic.snoozeloo.theme.Medium_Grey
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue

@Composable
fun SnoozelooActionButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    text: String,
    largeText: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        shape = RoundedCornerShape(26.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Snoozeloo_Blue,
            contentColor = Color.White,
            disabledContainerColor = Medium_Grey,
            disabledContentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = if (largeText) 24.sp else TextUnit.Unspecified
        )
    }
}

@Preview
@Composable
private fun SnoozelooActionButtonPreview() {
    SnoozelooActionButton(
        modifier = Modifier,
        text = "Save",
        onClick = {  }
    )
}

@Preview
@Composable
private fun SnoozelooActionButtonDisabledPreview() {
    SnoozelooActionButton(
        modifier = Modifier,
        enabled = false,
        text = "Save",
        onClick = {  }
    )
}

@Preview
@Composable
private fun SnoozelooActionButtonLargeTextPreview() {
    SnoozelooActionButton(
        modifier = Modifier
            .fillMaxWidth(),
        text = "Turn Off",
        largeText = true,
        onClick = {  }
    )
}