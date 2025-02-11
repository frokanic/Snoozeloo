package com.frokanic.snoozeloo.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.frokanic.snoozeloo.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface AlarmDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAlarm(alarm: AlarmEntity): Long

    @Query("SELECT * FROM alarmentity")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarmentity WHERE id = :id")
    suspend fun getAlarm(id: Int): AlarmEntity?

    @Query("UPDATE alarmentity SET isActive = NOT isActive WHERE id = :alarmId")
    suspend fun updateActiveStatus(alarmId: Int)

    @Query("UPDATE alarmentity SET timeStamp = :timeStamp WHERE id = :id")
    suspend fun updateTimeStamp(id: Int, timeStamp: LocalDateTime)

    @Query("DELETE FROM alarmentity WHERE id = :alarmId")
    suspend fun deleteAlarm(alarmId: Int)
}