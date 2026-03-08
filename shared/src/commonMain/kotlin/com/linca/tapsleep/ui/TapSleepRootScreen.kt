package com.linca.tapsleep.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.linca.tapsleep.ui.theme.TapSleepTheme

private sealed interface Screen {
    data object Picker : Screen
    data class Player(val sound: Sound) : Screen
}

@Composable
fun TapSleepRootScreen() {
    var screen by remember { mutableStateOf<Screen>(Screen.Picker) }

    TapSleepTheme {
        when (val s = screen) {
            is Screen.Picker -> MainScreen(
                onSoundClick = { sound -> screen = Screen.Player(sound) },
            )
            is Screen.Player -> PlayerScreen(
                sound  = s.sound,
                onBack = { screen = Screen.Picker },
            )
        }
    }
}
