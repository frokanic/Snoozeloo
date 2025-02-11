package snoozeloo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val id = intent?.getIntExtra("ID", -1)

        Log.d("AlarmReceiver", "The id is $id")

        val ringingIntent = Intent(context, AlarmRingingActivity::class.java).apply {
            putExtra("ID", id)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context?.startActivity(ringingIntent)
    }
}