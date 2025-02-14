package com.frokanic.snoozeloo.allalarms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.frokanic.snoozeloo.DayPeriod
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Light_Grey
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue
import com.frokanic.snoozeloo.allalarms.component.AlarmCard
import com.frokanic.snoozeloo.allalarms.component.DeleteAlarmDialog
import com.frokanic.snoozeloo.designsystem.R

@Composable
internal fun AllAlarmsScreen(
    viewModel: AllAlarmsViewModel = hiltViewModel(),
    navigateToDetails: (Int?) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is AllAlarmsEvent.AlarmPressed) {
                navigateToDetails(event.id)
            }
        }
    }

    AllAlarmsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AllAlarmsScreen(
    state: AllAlarmsUiData,
    onAction: (AllAlarmsEvent) -> Unit
) {
    if (!state.isLoading) {
        if (state.deleteAlarmDialogStatus.status) {
            DeleteAlarmDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp
                    ),
                onCancel = {
                    onAction(AllAlarmsEvent
                        .OnDeleteAlarmAlertDialogVisible(
                            id = null,
                            canceled = true
                        )
                    )
                },
                onCompleteDeletion = {
                    onAction(AllAlarmsEvent
                        .OnDeleteAlarmAlertDialogVisible(
                            id = null,
                            canceled = false
                        )
                    )
                }
            )
        }
        Scaffold(
            topBar = {
                Text(
                    modifier = Modifier
                        .padding(16.dp)
                        .statusBarsPadding(),
                    text = "Your Alarms",
                    fontSize = 24.sp,
                    color = Faded_Black
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onAction(AllAlarmsEvent.AlarmPressed()) },
                    shape = CircleShape,
                    containerColor = Snoozeloo_Blue,
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            containerColor = Light_Grey
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                verticalArrangement = Arrangement
                    .spacedBy(12.dp)
            ) {
                items(state.alarmDetails) { item ->
                    if (item.time != null
                        && item.period != null
                        && item.timeTillNextAlarm != null
                        && item.isActive != null
                    ) {
                        AlarmCard(
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                            id = item.id,
                            name = item.name,
                            time = item.time,
                            period = item.period,
                            alarmInTime = item.timeTillNextAlarm,
                            isActive = item.isActive,
                            onUpdateActiveStatus = {
                                onAction(
                                    AllAlarmsEvent.AlarmActiveStatusChanged(
                                        it
                                    )
                                )
                            },
                            onAlarmClick = { onAction(AllAlarmsEvent.AlarmPressed(it)) },
                            onDeleteAlarm = {
                                onAction(
                                    AllAlarmsEvent.OnDeleteAlarmAlertDialogVisible(
                                        id = it
                                    )
                                )
                            }
                        )
                    }
                }

                if (state.alarmDetails.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    imageVector = ImageVector
                                        .vectorResource(
                                            id = R.drawable.ic_alarm
                                        ),
                                    contentDescription = null
                                )

                                Spacer(
                                    modifier = Modifier
                                        .size(16.dp)
                                )

                                Text(
                                    text = "It's empty! Add the first alarm so you\n" +
                                            "don't miss an important moment!",
                                    fontSize = 16.sp,
                                    color = Faded_Black,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AllAlarmsScreenPreview() {
    AllAlarmsScreen(
        state = AllAlarmsUiData(
            isLoading = false,
            alarmDetails = listOf(
                AlarmItemDetails(
                    id = 1,
                    time = "07:35",
                    period = DayPeriod.AM,
                    timeTillNextAlarm = "Alarm in 8h 24m",
                    name = "Wakey wakey!",
                    isActive = true
                ),
                AlarmItemDetails(
                    id = 2,
                    time = "07:05",
                    period = DayPeriod.AM,
                    timeTillNextAlarm = "Alarm in 8h 00m",
                    name = null,
                    isActive = true
                )
            )
        ),
        onAction = {  }
    )
}

@Preview(showBackground = true)
@Composable
private fun AllAlarmsScreenEmptyListPreview() {
    AllAlarmsScreen(
        state = AllAlarmsUiData(
            isLoading = false,
            alarmDetails = emptyList()
        ),
        onAction = {  }
    )
}