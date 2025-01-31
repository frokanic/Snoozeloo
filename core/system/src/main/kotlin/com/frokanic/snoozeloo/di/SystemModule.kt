package com.frokanic.snoozeloo.di

import android.content.Context
import com.frokanic.snoozeloo.alarm.AlarmScheduler
import com.frokanic.snoozeloo.alarm.AlarmSchedulerImpl
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

}