package snoozeloo.ringing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.frokanic.snoozeloo.component.SnoozelooActionButton
import com.frokanic.snoozeloo.designsystem.R
import com.frokanic.snoozeloo.theme.Faded_Black
import com.frokanic.snoozeloo.theme.Snoozeloo_Blue

@Composable
internal fun AlarmRingingScreen(
    viewModel: AlarmRingingViewModel = hiltViewModel(),
    onCloseAlarmRingingScreen: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        viewModel.uiEvent.collect { event ->
            if (event is AlarmRingingEvent.TurnOff) {
                onCloseAlarmRingingScreen()
            }
        }
    }

    AlarmRingingScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun AlarmRingingScreen(
    state: AlarmRingingUiData,
    onAction: (AlarmRingingEvent) -> Unit
) {
    if (!state.loading) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    color = Color.White
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = ImageVector
                    .vectorResource(
                        id = R.drawable.ic_alarm
                    ),
                contentDescription = null
            )

            Spacer(
                modifier = Modifier
                    .size(16.dp)
            )

            Text(
                text = state.alarmData.time.orEmpty(),
                fontSize = 82.sp,
                color = Snoozeloo_Blue,
            )

            Spacer(
                modifier = Modifier
                    .size(16.dp)
            )

            Text(
                text = state.alarmData.name.orEmpty(),
                fontSize = 24.sp,
                color = Faded_Black,
            )

            Spacer(
                modifier = Modifier
                    .size(16.dp)
            )

            SnoozelooActionButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 84.dp
                    ),
                enabled = true,
                text = "Turn Off",
                largeText = true,
                onClick = {
                    onAction(AlarmRingingEvent.TurnOff)
                }
            )
        }
    }
}

@Preview
@Composable
private fun AlarmRingingScreenPreview() {
    AlarmRingingScreen(
        state = AlarmRingingUiData(
            loading = false,
            alarmData = AlarmData(
                time = "10:00",
                name = "Work"
            )
        ),
        onAction = {  }
    )
}