package snoozeloo.ringing

data class AlarmRingingUiData(
    val loading: Boolean = true,
    val alarmData: AlarmData = AlarmData(),
)

data class AlarmData(
    val time: String? = null,
    val name: String? = null,
)