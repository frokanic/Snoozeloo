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
    ): Int

    suspend fun updateActiveStatus(alarmId: Int)

    suspend fun updateTimeStamp(id: Int, timeStamp: LocalDateTime)

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
    ): Int {
        val alarm = AlarmEntity(
            id = id,
            desiredTimeHour = desiredTimeHour,
            desiredTimeMinute = desiredTimeMinute,
            timeStamp = timeStamp,
            name = name,
            isActive = isActive
        )

        return dao.saveAlarm(alarm).toInt()
    }

    override suspend fun updateActiveStatus(alarmId: Int) {
        dao.updateActiveStatus(alarmId)
    }

    override suspend fun updateTimeStamp(id: Int, timeStamp: LocalDateTime) {
        dao.updateTimeStamp(id, timeStamp)
    }

    override suspend fun deleteAlarm(alarmId: Int) {
        dao.deleteAlarm(alarmId = alarmId)
    }

}