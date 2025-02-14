package snoozeloo.ringing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frokanic.snoozeloo.repository.AlarmsRepository
import com.frokanic.snoozeloo.repository.RingtoneRepository
import com.frokanic.snoozeloo.repository.SystemAlarmRepository
import com.frokanic.snoozeloo.repository.VibrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmRingingViewModel @Inject constructor(
    private val alarmsRepository: AlarmsRepository,
    private val systemAlarmRepository: SystemAlarmRepository,
    private val ringtoneRepository: RingtoneRepository,
    private val vibrationRepository: VibrationRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var alarmId: Int? = savedStateHandle["id"]

     private val _uiState = MutableStateFlow(AlarmRingingUiData())
     val uiState = _uiState.asStateFlow()

     private val _uiEvent: MutableSharedFlow<AlarmRingingEvent?> = MutableSharedFlow()
     val uiEvent = _uiEvent.asSharedFlow()

     fun onAction(action: AlarmRingingEvent) {
         when (action) {
             AlarmRingingEvent.Ring -> initializeRinging(alarmId = alarmId)
             AlarmRingingEvent.TurnOff -> turnOff()
         }
     }

    init {
        onAction(AlarmRingingEvent.Ring)
    }

    private fun initializeRinging(alarmId: Int?) {
        viewModelScope.launch {
            if (alarmId != null) {
                alarmsRepository.getAlarm(alarmId)?.let { alarm ->
                    val formattedTime = formatTime(alarm.desiredTimeHour, alarm.desiredTimeMinute)

                    _uiState.update { state ->
                        state.copy(
                            loading = false,
                            alarmData = AlarmData(
                                time = formattedTime,
                                name = alarm.name,
                                timeStamp = alarm.timeStamp
                            )
                        )
                    }

                    systemAlarmRepository
                        .cancelAlarm(
                            id = alarmId,
                            timeStamp = alarm.timeStamp
                        )

//                    ringtoneRepository.play()
//                    vibrationRepository.vibrate()
                }
            }
        }
    }

    private fun turnOff() {
        viewModelScope.launch {
            _uiEvent.emit(AlarmRingingEvent.TurnOff)

//            ringtoneRepository.stop()
//            vibrationRepository.stop()
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val hourStr = hour.toString().padStart(2, '0')
        val minuteStr = minute.toString().padStart(2, '0')
        return "$hourStr:$minuteStr"
    }
}