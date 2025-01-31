package com.frokanic.snoozeloo.ringing

sealed interface AlarmRingingEvent {

    data object Ring: AlarmRingingEvent

    data object TurnOff: AlarmRingingEvent

}