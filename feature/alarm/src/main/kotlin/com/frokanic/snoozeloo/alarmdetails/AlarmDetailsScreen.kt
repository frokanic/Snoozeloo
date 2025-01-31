package com.frokanic.snoozeloo.alarmdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.frokanic.snoozeloo.alarmdetails.component.NameDialog
import com.frokanic.snoozeloo.alarmdetails.component.TimeInputBox
import com.frokanic.snoozeloo.designsystem.R
import com.frokanic.snoozeloo.component.SnoozelooActionButton
import com.frokanic.snoozeloo.theme.Dark_Grey
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Light_Grey
import com.frokanic.snoozeloo.theme.Medium_Grey

@Composable
internal fun AlarmDetailsScreen(
    viewModel: AlarmDetailsViewModel = hiltViewModel(),
    navigateToAllAlarms: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    
    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is AlarmDetailsEvent.OnExitScreen) {
                navigateToAllAlarms()
            }
        }
    }

    AlarmDetailsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AlarmDetailsScreen(
    state: AlarmDetailsUiData,
    onAction: (AlarmDetailsEvent) -> Unit
) {
    if (state.isLoading) {
        // Todo evaluate if any kind of management is merited.
    } else {
        
        if (state.alarmDetails.displayNameDialog) {
            NameDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    ),
                name = state.alarmDetails.name,
                onUpdateName = { name ->
                    onAction(
                        AlarmDetailsEvent.OnUpdateTitle(
                            name = name
                        )
                    )
                },
                onClose = {
                    onAction(AlarmDetailsEvent.OnUpdateTitleAlertVisibility)
                }
            )
        }
        
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .statusBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable {
                                onAction(AlarmDetailsEvent.OnNavigateBack(false))
                            },
                        imageVector = ImageVector
                            .vectorResource(id = R.drawable.ic_discard),
                        contentDescription = null,

                        )

                    SnoozelooActionButton(
                        modifier = Modifier
                            .height(36.dp),
                        enabled = state.alarmDetails.hoursInputted && state.alarmDetails.minutesInputted,
                        text = "Save",
                        onClick = {
                            onAction(AlarmDetailsEvent.OnSave)
                        }
                    )
                }
            },
            containerColor = Light_Grey
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp,
                            horizontal = 24.dp
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
//                        .height(160.dp) // Todo remove when I add the textfields
                ) {
                    Column {
                        // TODO NOT PROPERLY IMPLEMENTED

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 24.dp,
                                    bottom = 24.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TimeInputBox(
                                range = 0..23,
                                initialText = state.alarmDetails.hours.toString(),
                                onValueChange = { newValue ->
                                    onAction(
                                        AlarmDetailsEvent.OnUpdateTime(
                                            type = TimeType.Hour,
                                            time = newValue
                                        )
                                    )
                                }
                            )

                            TimeInputBox(
                                range = 0..59,
                                initialText = state.alarmDetails.minutes.toString(),
                                onValueChange = { newValue ->
                                    onAction(
                                        AlarmDetailsEvent.OnUpdateTime(
                                            type = TimeType.Minute,
                                            time = newValue
                                        )
                                    )
                                }
                            )
                        }

                        if (state.alarmDetails.timeTillNextAlarm != null) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        bottom = 16.dp
                                    ).align(Alignment.CenterHorizontally),
                                text = state.alarmDetails.timeTillNextAlarm,
                                fontSize = 16.sp,
                                color = Dark_Grey
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
//                        vertical = 16.dp,
                            horizontal = 24.dp
                        )
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 20.dp
                            ),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Alarm Name",
                            fontSize = 16.sp,
                            color = Faded_Black
                        )

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onAction(AlarmDetailsEvent.OnUpdateTitleAlertVisibility)
                                },
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = state.alarmDetails.name.orEmpty(),
                                fontSize = 16.sp,
                                color = Medium_Grey
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AlarmDetailsScreenPreview() {
    AlarmDetailsScreen(
        state = AlarmDetailsUiData(
            isLoading = false,
            alarmDetails = AlarmDetails(
                hours = 1,
                hoursInputted = true,
                minutes = 12,
                minutesInputted = true,
                timeTillNextAlarm = "Alarm in 4h 31m",
                name = null
            )
        ),
        onAction = {  },
    )
}