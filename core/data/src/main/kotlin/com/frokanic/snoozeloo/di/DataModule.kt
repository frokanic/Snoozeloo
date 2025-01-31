package com.frokanic.snoozeloo.di

import com.frokanic.snoozeloo.alarm.AlarmScheduler
import com.frokanic.snoozeloo.dao.AlarmDao
import com.frokanic.snoozeloo.repository.AlarmsRepository
import com.frokanic.snoozeloo.repository.AlarmsRepositoryImpl
import com.frokanic.snoozeloo.repository.SystemAlarmRepository
import com.frokanic.snoozeloo.repository.SystemAlarmRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
//    @Singleton
    fun provideAlarmsRepository(
        dao: AlarmDao
    ): AlarmsRepository =
        AlarmsRepositoryImpl(
            dao = dao
        )

    @Provides
    fun provideSystemAlarmRepository(
        alarmScheduler: AlarmScheduler
    ): SystemAlarmRepository =
        SystemAlarmRepositoryImpl(
            alarmScheduler = alarmScheduler
        )
}