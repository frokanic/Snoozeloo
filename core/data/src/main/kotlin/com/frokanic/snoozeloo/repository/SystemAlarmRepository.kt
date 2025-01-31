package com.frokanic.snoozeloo.repository

import com.frokanic.snoozeloo.SystemAlarm
import com.frokanic.snoozeloo.alarm.AlarmScheduler
import java.time.LocalDateTime

interface SystemAlarmRepository {
    suspend fun setAlarm(hour: Int, minute: Int, timeStamp: LocalDateTime, name: String?)
    suspend fun cancelAlarm(hour: Int, minute: Int, timeStamp: LocalDateTime, name: String?)
}

class SystemAlarmRepositoryImpl(
    private val alarmScheduler: AlarmScheduler
) : SystemAlarmRepository {

    override suspend fun setAlarm(hour: Int, minute: Int, timeStamp: LocalDateTime, name: String?) {
        alarmScheduler.schedule(
            item = SystemAlarm(
                time = timeStamp,
                timeString = "$hour:$minute",
                name = name
            )
        )
    }

    override suspend fun cancelAlarm(hour: Int, minute: Int, timeStamp: LocalDateTime, name: String?) {
        alarmScheduler.cancel(
            item = SystemAlarm(
                time = timeStamp,
                timeString = "$hour:$minute",
                name = name
            )
        )
    }

}