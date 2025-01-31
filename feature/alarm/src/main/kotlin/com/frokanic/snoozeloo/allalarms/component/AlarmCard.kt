package com.frokanic.snoozeloo.allalarms.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frokanic.snoozeloo.DayPeriod
import com.frokanic.snoozeloo.theme.Dark_Grey
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlarmCard(
    modifier: Modifier = Modifier,
    id: Int?,
    name: String? = null,
    time: String,
    period: DayPeriod,
    alarmInTime: String,
    isActive: Boolean,
    onUpdateActiveStatus: (Int?) -> Unit,
    onAlarmClick: (Int?) -> Unit,
    onDeleteAlarm: (Int?) -> Unit,
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    onAlarmClick(id)
                },
                onLongClick = {
                    onDeleteAlarm(id)
                }
            ),
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            bottom = 10.dp
                        ),
                    text = name
                        .orEmpty(),
                    color = Faded_Black,
                    fontSize = 16.sp
                )

                Switch(
                    checked = isActive,
                    onCheckedChange = {
                        onUpdateActiveStatus(id)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = Snoozeloo_Blue
                            .copy(alpha = 0.3f),
                        checkedTrackColor = Snoozeloo_Blue,
                        checkedThumbColor = Color.White,
                        uncheckedThumbColor = Color.White,
                        uncheckedBorderColor = Color.Transparent
                    ),
                    thumbContent = {  }
                )
            }
            Row(
                horizontalArrangement = Arrangement
                    .spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = time,
                    color = Faded_Black,
                    fontSize = 42.sp
                )

                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    text = period.name,
                    color = Faded_Black,
                    fontSize = 24.sp
                )
            }

            Spacer(
                modifier = Modifier
                    .size(8.dp)
            )

            Text(
                text = alarmInTime,
                color = Dark_Grey
            )
        }
    }
}

@Preview
@Composable
private fun AlarmCardPreview() {
    AlarmCard(
        id = 1,
        name = "Wake Up",
        time = "10:00",
        period = DayPeriod.AM,
        alarmInTime = "1h 30min",
        isActive = true,
        onUpdateActiveStatus = {  },
        onAlarmClick = {  },
        onDeleteAlarm = {  }
    )
}

@Preview
@Composable
private fun AlarmCardInactivePreview() {
    AlarmCard(
        id = 3,
        name = null,
        time = "10:00",
        period = DayPeriod.AM,
        alarmInTime = "1h 30min",
        isActive = false,
        onUpdateActiveStatus = {  },
        onAlarmClick = {  },
        onDeleteAlarm = {  }
    )
}