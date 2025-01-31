package com.frokanic.snoozeloo.ringing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.frokanic.snoozeloo.repository.SystemAlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmRingingViewModel @Inject constructor(
    private val systemAlarmRepository: SystemAlarmRepository,
) : ViewModel() {

     private val _uiState = MutableStateFlow(AlarmRingingUiData())
     val uiState = _uiState.asStateFlow()

     private val _uiEvent: MutableSharedFlow<AlarmRingingEvent?> = MutableSharedFlow()
     val uiEvent = _uiEvent.asSharedFlow()

     fun onAction(action: AlarmRingingEvent) {
         when (action) {
             AlarmRingingEvent.Ring -> initializeRinging()
             AlarmRingingEvent.TurnOff -> turnOff()
         }
     }

    private fun initializeRinging() {

    }

    private fun turnOff() {
        viewModelScope.launch {
            _uiEvent.emit(AlarmRingingEvent.TurnOff)
        }
    }
}