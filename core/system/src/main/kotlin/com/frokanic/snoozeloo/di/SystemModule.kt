package com.frokanic.snoozeloo.di

import android.content.Context
import com.frokanic.snoozeloo.alarm.AlarmScheduler
import com.frokanic.snoozeloo.alarm.AlarmSchedulerImpl
import com.frokanic.snoozeloo.ringtone.RingtonePlayer
import com.frokanic.snoozeloo.ringtone.RingtonePlayerImpl
import com.frokanic.snoozeloo.vibration.VibrationManager
import com.frokanic.snoozeloo.vibration.VibrationManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SystemModule {

    @Provides
    fun provideAlarmScheduler(
        @ApplicationContext context: Context
    ): AlarmScheduler =
            AlarmSchedulerImpl(
                context = context
            )

    @Provides
    fun provideRingtonePlayer(
        @ApplicationContext context: Context
    ): RingtonePlayer =
        RingtonePlayerImpl(
            context = context
        )

    @Provides
    fun provideVibrationManager(
        @ApplicationContext context: Context
    ): VibrationManager =
        VibrationManagerImpl(
            context = context
        )

}