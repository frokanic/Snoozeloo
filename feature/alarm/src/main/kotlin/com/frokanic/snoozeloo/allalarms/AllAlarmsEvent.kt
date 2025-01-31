package com.frokanic.snoozeloo.allalarms

sealed interface AllAlarmsEvent {

    data object FetchAlarms : AllAlarmsEvent

    data class OnDeleteAlarmAlertDialogVisible(
        val id: Int? = null,
        val canceled: Boolean = false
    ) : AllAlarmsEvent

    data class AlarmPressed(
        val id: Int? = null
    ) : AllAlarmsEvent

    data class AlarmActiveStatusChanged(
        val id: Int?
    ) : AllAlarmsEvent

}