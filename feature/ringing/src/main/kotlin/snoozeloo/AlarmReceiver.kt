package snoozeloo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra("ID", -1)

        context?.let {
            val serviceIntent = Intent(it, AlarmService::class.java).apply {
                putExtra("ID", id)
            }
            it.startForegroundService(serviceIntent)
        }
    }
}
