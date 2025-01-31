package com.frokanic.snoozeloo.alarmdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frokanic.snoozeloo.formatTimeForUi
import com.frokanic.snoozeloo.getNextDateTime
import com.frokanic.snoozeloo.repository.AlarmsRepository
import com.frokanic.snoozeloo.repository.SystemAlarmRepository
import com.frokanic.snoozeloo.updateTime
import com.frokanic.snoozeloo.usecase.CountDownTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AlarmDetailsViewModel @Inject constructor(
    private val alarmsRepository: AlarmsRepository,
    private val systemAlarmRepository: SystemAlarmRepository,
    private val countDownTimer: CountDownTimerUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var alarmId: Int? = savedStateHandle["id"]

    private val _uiState = MutableStateFlow(AlarmDetailsUiData())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<AlarmDetailsEvent?> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onAction(action: AlarmDetailsEvent) {
        when (action) {
            is AlarmDetailsEvent.FetchAlarm ->
                fetchAlarm(
                    id = alarmId
                )

            is AlarmDetailsEvent.OnNavigateBack -> navigateBack(save = action.save)

            AlarmDetailsEvent.OnSave -> saveAlarm()


            is AlarmDetailsEvent.OnUpdateTime ->
                updateTime(
                    type = action.type,
                    time = action.time.toIntOrNull()
                )

            AlarmDetailsEvent.ToggleTimer -> toggleTimer()

            AlarmDetailsEvent.OnUpdateTitleAlertVisibility -> updateTitleAlertVisibility()

            is AlarmDetailsEvent.OnUpdateTitle ->
                updateTitle(
                    name = action.name
                )

            AlarmDetailsEvent.OnExitScreen ->
                viewModelScope.launch {
                    _uiEvent.emit(action)
                }
        }
    }

    init {
        onAction(action = AlarmDetailsEvent.FetchAlarm(id = alarmId))
        observeTimer()
        toggleTimer()
    }

    private fun updateAlarm(
        isLoading: Boolean? = null,
        id: Int? = null,
        hours: Int? = null,
        hoursInputted: Boolean? = null,
        minutes: Int? = null,
        minutesInputted: Boolean? = null,
        timeTillNextAlarm: String? = null,
        displayNameDialog: Boolean? = null,
        name: String? = null
    ) {
        _uiState.update { current ->
            current.copy(
                isLoading = isLoading ?: current.isLoading,
                alarmDetails = AlarmDetails(
                    id = id ?: current.alarmDetails.id,
                    hours = hours ?: current.alarmDetails.hours,
                    hoursInputted = hoursInputted ?: current.alarmDetails.hoursInputted,
                    minutes = minutes ?: current.alarmDetails.minutes,
                    minutesInputted = minutesInputted ?: current.alarmDetails.minutesInputted,
                    timeTillNextAlarm = if (!current.alarmDetails.hoursInputted || !current.alarmDetails.hoursInputted)
                        null
                    else
                        timeTillNextAlarm ?: current.alarmDetails.timeTillNextAlarm,
                    displayNameDialog = displayNameDialog ?: current.alarmDetails.displayNameDialog,
                    name = name ?: current.alarmDetails.name
                )
            )
        }
    }

    private fun fetchAlarm(id: Int?) {
        if (id != null) {
            viewModelScope.launch {
                val alarm = alarmsRepository
                    .getAlarm(
                        id = id
                    )

                countDownTimer.startTimer()

                val timeUntil = Pair(alarm?.desiredTimeHour, alarm?.desiredTimeMinute).updateTime()

                if (alarm != null) {
                    updateAlarm(
                        isLoading = false,
                        id = id,
                        hours = alarm.desiredTimeHour,
                        hoursInputted = true,
                        minutes = alarm.desiredTimeMinute,
                        minutesInputted = true,
                        timeTillNextAlarm = timeUntil.formatTimeForUi(),
                        name = alarm.name,
                        displayNameDialog = false
                    )
                } else {
                    updateAlarm(
                        isLoading = false
                    )
                }
            }
        } else {
            updateAlarm(
                isLoading = false
            )
        }
    }

    private fun updateTime(type: TimeType, time: Int?) {
        when (type) {
            TimeType.Hour -> updateAlarm(
                hours = time,
                hoursInputted = time != null
            )
            TimeType.Minute -> updateAlarm(
                minutes = time,
                minutesInputted = time != null
            )
        }
    }

    private fun toggleTimer() {
        viewModelScope.launch {
            _uiState.collectLatest { state ->
                if (state.alarmDetails.hoursInputted && state.alarmDetails.minutesInputted) {
                    countDownTimer.startTimer()
                } else {
                    countDownTimer.stopTimer()

                    updateAlarm(
                        timeTillNextAlarm = null,
                    )
                }
            }
        }
    }

    private fun observeTimer() {
        viewModelScope.launch {
            countDownTimer.timer.onEach {
                val timeUntil = Pair(
                    _uiState.value.alarmDetails.hours!!,
                    _uiState.value.alarmDetails.minutes!!
                ).updateTime()

                updateAlarm(
                    hoursInputted = _uiState.value.alarmDetails.hoursInputted,
                    minutesInputted = _uiState.value.alarmDetails.minutesInputted,
                    timeTillNextAlarm = timeUntil.formatTimeForUi(),
                )
            }.launchIn(viewModelScope)
        }
    }

    private fun updateTitleAlertVisibility() {
        updateAlarm(
            displayNameDialog = !_uiState.value.alarmDetails.displayNameDialog,
        )
    }

    private fun updateTitle(name: String) {
        updateAlarm(
            displayNameDialog = false,
            name = name
        )
    }

    private fun saveAlarm() {
        viewModelScope.launch {
            val currentAlarm = _uiState.value.alarmDetails

            if (currentAlarm.hours != null && currentAlarm.minutes != null) {
                val timeStamp = Pair(currentAlarm.hours, currentAlarm.minutes).getNextDateTime()

                alarmsRepository.saveAlarm(
                    id = currentAlarm.id,
                    desiredTimeHour = currentAlarm.hours,
                    desiredTimeMinute = currentAlarm.minutes,
                    timeStamp = timeStamp,
                    name = currentAlarm.name,
                    isActive = true
                )

                setSystemAlarm(
                    hours = currentAlarm.hours,
                    minutes = currentAlarm.minutes,
                    timeStamp = timeStamp,
                    name = currentAlarm.name
                )

                onAction(AlarmDetailsEvent.OnNavigateBack(save = true))
            }
        }
    }

    private fun navigateBack(save: Boolean) {
        viewModelScope.launch {
            val id = _uiState.value.alarmDetails.id
            val hour = uiState.value.alarmDetails.hours
            val minute = uiState.value.alarmDetails.minutes

            if (save) {
                if (hour != null && minute != null) {
                    cancelSystemAlarm(
                        hour = hour,
                        minute = minute,
                        timeStamp = Pair(
                            hour,
                            minute
                        ).getNextDateTime(),
                        name = _uiState.value.alarmDetails.name
                    )
                }
            } else if (id != null) {
                val oldAlarm = alarmsRepository
                    .getAlarm(
                        id = id
                    )

                if (oldAlarm != null) {
                    cancelSystemAlarm(
                        hour = oldAlarm.desiredTimeHour,
                        minute = oldAlarm.desiredTimeMinute,
                        timeStamp = Pair(
                            oldAlarm.desiredTimeHour,
                            oldAlarm.desiredTimeMinute
                        ).getNextDateTime(),
                        name = _uiState.value.alarmDetails.name
                    )
                }
            }

            onAction(AlarmDetailsEvent.OnExitScreen)
        }
    }

    private fun setSystemAlarm(hours: Int, minutes: Int, timeStamp: LocalDateTime, name: String?) {
        viewModelScope.launch {
            systemAlarmRepository.setAlarm(
                hour = hours,
                minute = minutes,
                timeStamp = timeStamp,
                name = name
            )
        }
    }

    private fun cancelSystemAlarm(hour: Int, minute: Int, timeStamp: LocalDateTime, name: String?) {
        viewModelScope.launch {
            systemAlarmRepository.cancelAlarm(
                hour = hour,
                minute = minute,
                timeStamp = timeStamp,
                name = name
            )
        }
    }
}