package com.frokanic.snoozeloo.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val time = intent?.getStringExtra("TIME")// ?: return
        val name = intent?.getStringExtra("NAME")// ?: return
    }
}