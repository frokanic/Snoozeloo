package snoozeloo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.frokanic.snoozeloo.repository.RingtoneRepository
import com.frokanic.snoozeloo.repository.VibrationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class AlarmService : Service() {

    @Inject
    lateinit var ringtoneRepository: RingtoneRepository
    @Inject
    lateinit var vibrationRepository: VibrationRepository

    companion object {
        private const val NOTIFICATION_ID = 1234
        private const val CHANNEL_ID = "alarm_channel"
        private const val CHANNEL_NAME = "Alarm Notifications"

        private const val ACTION_DISMISS_ALARM = "snoozeloo.ACTION_DISMISS_ALARM"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DISMISS_ALARM -> {
                stopSelf()
                return START_NOT_STICKY
            }
            else -> {
                val alarmId = intent?.getIntExtra("ID", -1) ?: -1

                ringtoneRepository.play()
                vibrationRepository.vibrate()

                startForeground(NOTIFICATION_ID, createAlarmNotification(alarmId))
                return START_NOT_STICKY
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ringtoneRepository.stop()
        vibrationRepository.stop()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createAlarmNotification(alarmId: Int): Notification {
        val fullScreenIntent = Intent(this, AlarmRingingActivity::class.java).apply {
            putExtra("ID", alarmId)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_DISMISS_ALARM
        }

        val dismissPendingIntent = PendingIntent.getService(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("Your alarm is ringing!")
            .setSmallIcon(com.frokanic.snoozeloo.designsystem.R.drawable.ic_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(false)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setDeleteIntent(dismissPendingIntent)
            .build()
    }
}