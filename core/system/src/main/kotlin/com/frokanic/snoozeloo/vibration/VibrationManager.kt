package com.frokanic.snoozeloo.vibration

import android.annotation.SuppressLint
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

interface VibrationManager {
    fun vibrate()
    fun stop()
}

@SuppressLint("MissingPermission")
class VibrationManagerImpl(
    context: Context
) : VibrationManager {

    private var vibrator = context.getSystemService(Vibrator::class.java)

    override fun vibrate() {
        val pattern = longArrayOf(0, 500, 1000)
        val repeatIndex = 0

        vibrator?.let { vib ->
            val vibrationEffect = VibrationEffect.createWaveform(pattern, repeatIndex)
            vib.vibrate(vibrationEffect)
        }
    }

    override fun stop() {
        vibrator?.cancel()
        vibrator = null
    }

}