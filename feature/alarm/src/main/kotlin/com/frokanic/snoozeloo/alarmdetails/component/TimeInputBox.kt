package com.frokanic.snoozeloo.alarmdetails.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.insert
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frokanic.snoozeloo.theme.Dark_Grey
import com.frokanic.snoozeloo.theme.Light_Grey
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue

@Composable
fun TimeInputBox(
    modifier: Modifier = Modifier,
    range: IntRange,
    initialText: String = "",
    onValueChange: (String) -> Unit
) {
    val state = rememberTextFieldState(
        initialText = if (initialText.toIntOrNull() != null) initialText else ""
    )

    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .collect { newText ->
                val numeric = newText.toString().toIntOrNull()
                if (numeric in range || numeric == null) {
                    onValueChange(newText.toString())
                }
            }
    }

    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        state = state,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
//                if (isFocused && !focusState.isFocused) {
//                    onValueChange(state.text.toString())
//                }
//                isFocused = focusState.isFocused
            },
        enabled = true,
        readOnly = false,
        inputTransformation = {
            val typedText = asCharSequence().toString()

            if (typedText.isNotEmpty()) {
                val numericValue = typedText.toIntOrNull()
                if (numericValue == null || numericValue !in range) {
                    revertAllChanges()
                }
            }
        },
        textStyle = TextStyle(
            color = Snoozeloo_Blue,
            fontSize = 52.sp,
            textAlign = TextAlign.Center,
        ),
        keyboardOptions = KeyboardOptions(
//            capitalization = KeyboardCapitalization.None,
//            autoCorrectEnabled = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
//            platformImeOptions = PlatformImeOptions(
//                privateImeOptions = TODO()
//            ),
            showKeyboardOnFocus = false,
//            hintLocales = TODO()
        ),
//        onKeyboardAction = onKeyboardAction,
        lineLimits = TextFieldLineLimits.SingleLine,
        onTextLayout = null,
        interactionSource = null,
        cursorBrush = SolidColor(
            Snoozeloo_Blue
        ),
        outputTransformation = {
            if (length == 1) {
                insert(0, "0")
            }
        },
        decorator = { innerTextField ->
            Card(
                modifier = Modifier
                    .width(128.dp)
                    .height(95.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Light_Grey
                ),
                // Todo add some border related animation maybe
            ) {
                Box(
                    modifier = Modifier
                        .padding(
                            horizontal = ((95 - 52) / 2).dp,
                            vertical = ((95 - 52) / 2).dp,
                        )
                        .fillMaxSize()
                ) {
                    if (state.text.isBlank()) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "00",
                            color = Dark_Grey,
                            fontSize = 52.sp
                        )
                    } else {
                        innerTextField()
                    }
                }
            }
        },
        scrollState = ScrollState(
            initial = 0
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun TimeInputBoxPreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TimeInputBox(
            range = 0..23,
            onValueChange = {  }
        )

        TimeInputBox(
            range = 0..59,
            onValueChange = {  }
        )
    }
}