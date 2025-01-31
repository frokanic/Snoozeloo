package com.frokanic.snoozeloo.allalarms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frokanic.snoozeloo.formatTimeForUi
import com.frokanic.snoozeloo.repository.AlarmsRepository
import com.frokanic.snoozeloo.repository.SystemAlarmRepository
import com.frokanic.snoozeloo.toFormattedTimePair
import com.frokanic.snoozeloo.updateTime
import com.frokanic.snoozeloo.usecase.CountDownTimerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AllAlarmsViewModel @Inject constructor(
    private val alarmsRepository: AlarmsRepository,
    private val systemAlarmRepository: SystemAlarmRepository,
    private val countDownTimer: CountDownTimerUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AllAlarmsUiData())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: MutableSharedFlow<AllAlarmsEvent?> = MutableSharedFlow()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onAction(action: AllAlarmsEvent) {
        when (action) {
            AllAlarmsEvent.FetchAlarms -> fetchAlarms()

            is AllAlarmsEvent.AlarmActiveStatusChanged -> updateAlarmActiveStatus(action.id)

            is AllAlarmsEvent.AlarmPressed -> alarmPressed(id = action.id)

            is AllAlarmsEvent.OnDeleteAlarmAlertDialogVisible ->
                updateDeleteAlarmDialogVisibility(
                    id = action.id,
                    canceled = action.canceled
                )
        }
    }

    init {
        onAction(AllAlarmsEvent.FetchAlarms)
        observeTimer()
    }

    private fun fetchAlarms() {
        viewModelScope.launch {
            alarmsRepository.getAllAlarms()
                .map { userAlarms ->
                    userAlarms.map { userAlarm ->
                        val (formattedTime, period) =
                            Pair(userAlarm.desiredTimeHour, userAlarm.desiredTimeMinute)
                                .toFormattedTimePair()

                        AlarmItemDetails(
                            id = userAlarm.id,
                            hours = userAlarm.desiredTimeHour,
                            minutes = userAlarm.desiredTimeMinute,
                            time = formattedTime,
                            period = period,
                            isActive = userAlarm.isActive,
                            timeTillNextAlarm = Pair(userAlarm.desiredTimeHour, userAlarm.desiredTimeMinute)
                                .updateTime()
                                .formatTimeForUi(),
                            name = userAlarm.name
                        )
                    }
                }
                .collectLatest { alarmDetailsList ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            alarmDetails = alarmDetailsList
                        )
                    }
                }
        }
    }

    private fun observeTimer() {
        countDownTimer.startTimer()

        viewModelScope.launch {
            countDownTimer.timer.onEach {
                val updatedList = _uiState.value.alarmDetails.map { alarmItem ->
                    val timeUntil = Pair(
                        alarmItem.hours,
                        alarmItem.minutes
                    ).updateTime()

                    alarmItem.copy(
                        timeTillNextAlarm = timeUntil.formatTimeForUi()
                    )
                }

                _uiState.update { it.copy(alarmDetails = updatedList) }
            }.launchIn(viewModelScope)
        }
    }

    private fun updateAlarmActiveStatus(id: Int?) {
        if (id != null) {
            viewModelScope.launch {
                val alarmItemStatus = _uiState.value.alarmDetails.find { it.id == id }?.isActive

                if (alarmItemStatus == false) {
                    cancelSystemAlarm(id = id)
                } else {
                    setSystemAlarm(id = id)
                }

                alarmsRepository.updateActiveStatus(
                    alarmId = id
                )
            }
        }
    }

    private fun updateDeleteAlarmDialogVisibility(
        id: Int?,
        canceled: Boolean = false
    ) {
        val status = _uiState.value.deleteAlarmDialogStatus.status

        if (status) {
            if (!canceled) {
                deleteAlarm(_uiState.value.deleteAlarmDialogStatus.id)
            }

            _uiState.update {
                it.copy(
                    isLoading = _uiState.value.isLoading,
                    alarmDetails = _uiState.value.alarmDetails,
                    deleteAlarmDialogStatus = AlarmDeletionStatus(
                        status = false,
                        id = null
                    )
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isLoading = _uiState.value.isLoading,
                    alarmDetails = _uiState.value.alarmDetails,
                    deleteAlarmDialogStatus = AlarmDeletionStatus(
                        status = true,
                        id = id
                    )
                )
            }
        }
    }

    private fun deleteAlarm(id: Int?) {
        if (id != null) {
            viewModelScope.launch {
                alarmsRepository.deleteAlarm(
                    alarmId = id
                )
            }
        }
    }

    private fun alarmPressed(id: Int?) {
        viewModelScope.launch {
            _uiEvent.emit(
                AllAlarmsEvent
                    .AlarmPressed(
                        id = id
                    )
            )
        }
    }

    private suspend fun setSystemAlarm(id: Int) {
        val alarmItem = _uiState.value.alarmDetails.find { it.id == id }

        val hour = alarmItem?.hours
        val minute = alarmItem?.minutes
        val timeStamp = alarmItem?.timeStamp
        val name = alarmItem?.name

        if (hour != null && minute != null && timeStamp != null) {
            systemAlarmRepository.setAlarm(
                hour = hour,
                minute = minute,
                timeStamp = timeStamp,
                name = name
            )
        }
    }

    private suspend fun cancelSystemAlarm(id: Int) {
        val alarmItem = _uiState.value.alarmDetails.find { it.id == id }

        val hour = alarmItem?.hours
        val minute = alarmItem?.minutes
        val timeStamp = alarmItem?.timeStamp
        val name = alarmItem?.name

        if (hour != null && minute != null && timeStamp != null) {
            systemAlarmRepository.cancelAlarm(
                hour = hour,
                minute = minute,
                timeStamp = timeStamp,
                name = name
            )
        }
    }
}