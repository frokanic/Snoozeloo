package com.frokanic.snoozeloo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class AlarmEntity(
    @PrimaryKey(autoGenerate=true)
    val id: Int? = 0,
    val desiredTimeHour: Int,
    val desiredTimeMinute: Int,
    val timeStamp: LocalDateTime,
    val name: String? = null,
    val isActive: Boolean = true,
)
