package com.frokanic.snoozeloo.alarmdetails

sealed interface AlarmDetailsEvent {

    data class FetchAlarm(
        val id: Int?
    ) : AlarmDetailsEvent

    data class OnUpdateTime(
        val type: TimeType,
        val time: String
    ) : AlarmDetailsEvent

    data object ToggleTimer : AlarmDetailsEvent

    data object OnUpdateTitleAlertVisibility : AlarmDetailsEvent

    data class OnUpdateTitle(
        val name: String
    ) : AlarmDetailsEvent

    data object OnSave : AlarmDetailsEvent

    data class OnNavigateBack(
        val save: Boolean = false
    ) : AlarmDetailsEvent

    data object OnExitScreen : AlarmDetailsEvent
}