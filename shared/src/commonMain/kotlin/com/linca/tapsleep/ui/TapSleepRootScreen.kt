package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.linca.tapsleep.ui.theme.TapSleepTheme

private sealed interface Screen {
    data object Picker : Screen
    data object BlendPicker : Screen
    data class Player(val sound: Sound) : Screen
    data class BlendPlayer(val sounds: List<Sound>) : Screen
}

@Composable
fun TapSleepRootScreen() {
    var screen by remember { mutableStateOf<Screen>(Screen.Picker) }

    TapSleepTheme {
        when (val s = screen) {
            is Screen.Picker -> MainScreen(
                onSoundClick = { sound -> screen = Screen.Player(sound) },
                onBlendClick = { screen = Screen.BlendPicker },
            )
            is Screen.BlendPicker -> BlendScreen(
                onPlay = { sounds -> screen = Screen.BlendPlayer(sounds) },
                onBack = { screen = Screen.Picker },
            )
            is Screen.Player -> PlayerScreen(
                sound = s.sound,
                onBack = { screen = Screen.Picker },
            )
            is Screen.BlendPlayer -> BlendPlayerScreen(
                sounds = s.sounds,
                onBack = { screen = Screen.BlendPicker },
            )
        }
    }
}
