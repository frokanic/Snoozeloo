package com.frokanic.snoozeloo.repository

import com.frokanic.snoozeloo.ringtone.RingtonePlayer

interface RingtoneRepository {
    fun play()
    fun stop()
}

class RingtoneRepositoryImpl(
    private val ringtonePlayer: RingtonePlayer
) : RingtoneRepository {

    override fun play() {
        ringtonePlayer.play()
    }

    override fun stop() {
        ringtonePlayer.stop()
    }

}