package com.frokanic.snoozeloo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import com.frokanic.snoozeloo.AlarmDatabase
import com.frokanic.snoozeloo.dao.AlarmDao
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAlarmDatabase(
        @ApplicationContext context: Context
    ): AlarmDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                AlarmDatabase::class.java,
                "alarm_database"
            )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideAlarmDao(database: AlarmDatabase): AlarmDao =
        database.dao

}