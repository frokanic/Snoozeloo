package com.frokanic.snoozeloo

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.frokanic.snoozeloo.converter.LocalDateTimeConverter
import com.frokanic.snoozeloo.dao.AlarmDao
import com.frokanic.snoozeloo.entity.AlarmEntity

@Database(
    entities = [AlarmEntity::class],
    version = 1
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AlarmDatabase: RoomDatabase() {
    abstract val dao: AlarmDao
}