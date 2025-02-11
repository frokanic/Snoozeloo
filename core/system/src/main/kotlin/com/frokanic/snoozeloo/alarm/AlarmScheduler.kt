package com.frokanic.snoozeloo.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.frokanic.snoozeloo.SystemAlarm
import java.time.ZoneId

interface AlarmScheduler {
    fun schedule(item: SystemAlarm)
    fun cancel(item: SystemAlarm)
}

class AlarmSchedulerImpl(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)
    private val alarmAction = "com.frokanic.snoozeloo.ACTION_MY_ALARM"

    override fun schedule(item: SystemAlarm) {

        val intent = Intent(alarmAction).apply {
            `package` = context.packageName
            putExtra("ID", item.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager
            .setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                pendingIntent
            )
    }

    override fun cancel(item: SystemAlarm) {

        val intent = Intent(alarmAction).apply {
            `package` = context.packageName
            putExtra("ID", item.id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

}