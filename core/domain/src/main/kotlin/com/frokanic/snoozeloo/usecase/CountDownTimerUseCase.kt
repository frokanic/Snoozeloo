package com.frokanic.snoozeloo.usecase

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface CountDownTimerUseCase {

    val timer: SharedFlow<Unit>

    fun startTimer()

    fun stopTimer()
}

class CountDownTimerUseCaseImpl(
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : CountDownTimerUseCase {

    private val _timer = MutableSharedFlow<Unit>(replay = 0)
    override val timer: SharedFlow<Unit> = _timer.asSharedFlow()

    private var timerJob: Job? = null

    override fun startTimer() {
        if (timerJob?.isActive == true) return

        timerJob = coroutineScope.launch {
            while (isActive) {
                _timer.emit(Unit)
                delay(60_000)
            }
        }
    }

    override fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

}