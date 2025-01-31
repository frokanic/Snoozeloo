package com.frokanic.snoozeloo.alarmdetails

import androidx.compose.runtime.Immutable
import java.time.LocalDateTime

@Immutable
data class AlarmDetailsUiData(
    val isLoading: Boolean = true,
    val alarmDetails: AlarmDetails = AlarmDetails()
)

data class AlarmDetails(
    val id: Int? = null,
    val hours: Int? = null,
    val hoursInputted: Boolean = false,
    val minutes: Int? = null,
    val minutesInputted: Boolean = false,
    val timeTillNextAlarm: String? = null,
    val name: String? = null,
    val displayNameDialog: Boolean = false
)

enum class TimeType {
    Hour, Minute
}