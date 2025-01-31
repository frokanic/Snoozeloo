package com.frokanic.snoozeloo.repository

import com.frokanic.snoozeloo.UserAlarm
import com.frokanic.snoozeloo.dao.AlarmDao
import com.frokanic.snoozeloo.entity.AlarmEntity
import com.frokanic.snoozeloo.mapper.toUserAlarm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

interface AlarmsRepository {

    fun getAllAlarms(): Flow<List<UserAlarm>>

    suspend fun getAlarm(id: Int): UserAlarm?

    suspend fun saveAlarm(
        id: Int?,
        desiredTimeHour: Int,
        desiredTimeMinute: Int,
        timeStamp: LocalDateTime,
        name: String?,
        isActive: Boolean,
    )

    suspend fun updateActiveStatus(alarmId: Int)

    suspend fun deleteAlarm(alarmId: Int)
}

class AlarmsRepositoryImpl(
    val dao: AlarmDao
) : AlarmsRepository {

    override fun getAllAlarms(): Flow<List<UserAlarm>> =
        dao.getAllAlarms().map { entities ->
            entities.map { entity ->
                entity.toUserAlarm()
            }
        }

    override suspend fun getAlarm(id: Int): UserAlarm? {
        return dao.getAlarm(id)?.toUserAlarm()
    }

    override suspend fun saveAlarm(
        id: Int?,
        desiredTimeHour: Int,
        desiredTimeMinute: Int,
        timeStamp: LocalDateTime,
        name: String?,
        isActive: Boolean,
    ) {
        val alarm = AlarmEntity(
            id = id,
            desiredTimeHour = desiredTimeHour,
            desiredTimeMinute = desiredTimeMinute,
            timeStamp = timeStamp,
            name = name,
            isActive = isActive
        )

        dao.saveAlarm(alarm = alarm)
    }

    override suspend fun updateActiveStatus(alarmId: Int) {
        dao.updateActiveStatus(alarmId)
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        dao.deleteAlarm(alarmId = alarmId)
    }

}