package com.frokanic.snoozeloo.repository

import com.frokanic.snoozeloo.SystemAlarm
import com.frokanic.snoozeloo.alarm.AlarmScheduler
import java.time.LocalDateTime

interface SystemAlarmRepository {
    suspend fun setAlarm(id: Int, timeStamp: LocalDateTime)

    suspend fun cancelAlarm(id: Int, timeStamp: LocalDateTime)
}

class SystemAlarmRepositoryImpl(
    private val alarmScheduler: AlarmScheduler
) : SystemAlarmRepository {

    override suspend fun setAlarm(id: Int, timeStamp: LocalDateTime) {
        alarmScheduler.schedule(
            item = SystemAlarm(
                id = id,
                time = timeStamp
            )
        )
    }

    override suspend fun cancelAlarm(id: Int, timeStamp: LocalDateTime) {
        alarmScheduler.cancel(
            item = SystemAlarm(
                id = id,
                time = timeStamp
            )
        )
    }

}