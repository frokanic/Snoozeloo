package com.frokanic.snoozeloo.allalarms

import com.frokanic.snoozeloo.DayPeriod
import java.time.LocalDateTime

data class AllAlarmsUiData(
    val isLoading: Boolean = true,
    val alarmDetails: List<AlarmItemDetails> = emptyList(),
    val deleteAlarmDialogStatus: AlarmDeletionStatus = AlarmDeletionStatus()
)

data class AlarmItemDetails(
    val id: Int? = null,
    val time: String? = null,
    val period: DayPeriod? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val timeStamp: LocalDateTime? = null,
    val timeTillNextAlarm: String? = null,
    val name: String? = null,
    val isActive: Boolean? = null,
)

data class AlarmDeletionStatus(
    val status: Boolean = false,
    val id: Int? = null
)