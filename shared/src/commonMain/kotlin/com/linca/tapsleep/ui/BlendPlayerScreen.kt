package com.linca.tapsleep.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.linca.tapsleep.audio.rememberSoundPlayer
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.MoonGlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlendPlayerScreen(sounds: List<Sound>, onBack: () -> Unit) {
    val p0 = rememberSoundPlayer()
    val p1 = rememberSoundPlayer()
    val p2 = rememberSoundPlayer()
    val players = remember(sounds) { listOf(p0, p1, p2).take(sounds.size) }
    val volumes = remember(sounds) { sounds.map { mutableStateOf(1f) } }

    DisposableEffect(Unit) { onDispose { players.forEach { it.stop() } } }

    PlayerLayout(
        sounds = sounds,
        onStartPlayback = {
            players.forEachIndexed { i, p ->
                p.play(sounds[i].name.lowercase())
                p.setVolume(volumes[i].value)
            }
        },
        onResume = { players.forEach { it.resume() } },
        onPause = { players.forEach { it.pause() } },
        onStop = { players.forEach { it.stop() } },
        onBack = onBack,
        extraContent = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                sounds.forEachIndexed { i, sound ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = sound.icon,
                            contentDescription = sound.name,
                            tint = sound.tint.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            sound.name,
                            style = MaterialTheme.typography.labelMedium,
                            color = MoonGlow.copy(alpha = 0.5f),
                            modifier = Modifier.width(56.dp),
                        )
                        Slider(
                            value = volumes[i].value,
                            onValueChange = { v ->
                                volumes[i].value = v
                                players[i].setVolume(v)
                            },
                            modifier = Modifier.weight(1f).height(28.dp),
                            thumb = {
                                Box(
                                    modifier = Modifier.size(15.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(sound.tint),
                                    )
                                }
                            },
                            track = { sliderState ->
                                SliderDefaults.Track(
                                    sliderState = sliderState,
                                    modifier = Modifier.height(4.dp),
                                    colors = SliderDefaults.colors(
                                        activeTrackColor = sound.tint.copy(alpha = 0.7f),
                                        inactiveTrackColor = Dusk.copy(alpha = 0.2f),
                                    ),
                                )
                            },
                        )
                    }
                }
            }
        },
    )
}
