package com.frokanic.snoozeloo

import java.time.LocalDateTime

data class SystemAlarm(
    val time: LocalDateTime,
    val timeString: String,
    val name: String?
)
