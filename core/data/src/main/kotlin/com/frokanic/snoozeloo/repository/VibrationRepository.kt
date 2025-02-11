package com.frokanic.snoozeloo.repository

import com.frokanic.snoozeloo.vibration.VibrationManager

interface VibrationRepository {
    fun vibrate()
    fun stop()
}

class VibrationRepositoryImpl(
    private val vibrationManager: VibrationManager
) : VibrationRepository {

    override fun vibrate() {
        vibrationManager.vibrate()
    }

    override fun stop() {
        vibrationManager.stop()
    }

}