package com.frokanic.snoozeloo

import java.time.LocalDateTime

data class SystemAlarm(
    val time: LocalDateTime,
    val id: Int
)
