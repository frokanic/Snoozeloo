package com.frokanic.snoozeloo

import java.time.LocalDateTime

data class UserAlarm(
    val id: Int?,
    val name: String? = null,
    val isActive: Boolean = true,
    val desiredTimeHour: Int,
    val desiredTimeMinute: Int,
    val timeStamp: LocalDateTime,
)
