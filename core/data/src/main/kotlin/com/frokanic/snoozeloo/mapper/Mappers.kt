package com.frokanic.snoozeloo.mapper

import com.frokanic.snoozeloo.UserAlarm
import com.frokanic.snoozeloo.entity.AlarmEntity

fun AlarmEntity.toUserAlarm(): UserAlarm =
    UserAlarm(
        id = id,
        name = name,
        isActive = isActive,
        desiredTimeHour = desiredTimeHour,
        desiredTimeMinute = desiredTimeMinute,
        timeStamp = timeStamp
    )

fun UserAlarm.toAlarmEntity(): AlarmEntity =
    AlarmEntity(
        id = id ?: 0,
        name = name,
        isActive = isActive,
        desiredTimeHour = desiredTimeHour,
        desiredTimeMinute = desiredTimeMinute,
        timeStamp = timeStamp
    )