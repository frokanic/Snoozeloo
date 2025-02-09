package snoozeloo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val time = intent?.getStringExtra("TIME")
        val name = intent?.getStringExtra("NAME")

        val ringingIntent = Intent(context, AlarmRingingActivity::class.java).apply {
            putExtra("TIME", time)
            putExtra("NAME", name)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(ringingIntent)
    }
}