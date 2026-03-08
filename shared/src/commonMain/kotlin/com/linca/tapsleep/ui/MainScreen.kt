package com.linca.tapsleep.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.linca.tapsleep.ui.theme.AuroraPale
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Indigo
import com.linca.tapsleep.ui.theme.IndigoDark
import com.linca.tapsleep.ui.theme.Lavender
import com.linca.tapsleep.ui.theme.LavenderPale
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow
import com.linca.tapsleep.ui.theme.MoonWarm
import com.linca.tapsleep.ui.theme.Night

private data class Sound(val emoji: String, val name: String)

private val sounds = listOf(
    Sound("🌧", "Rain"),
    Sound("🌊", "Ocean"),
    Sound("🌲", "Forest"),
    Sound("🔥", "Fire"),
    Sound("💨", "Wind"),
    Sound("☕", "Café"),
    Sound("🌌", "Space"),
    Sound("🎵", "Brown"),
    Sound("🌙", "Night"),
)

private val SoundBtnBg = Color(0x0DC8C0A8)
private val SoundBtnBorder = Color(0x14C8C0A8)

@Composable
fun MainScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Night)
            .padding(horizontal = 24.dp),
    ) {

        Row(modifier = Modifier.fillMaxWidth()){

        }

        Spacer(Modifier.weight(0.5f))

        Text(
            "Tonight.",
            style = MaterialTheme.typography.headlineLarge,
            color = Moon,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "CHOOSE A SOUND",
            style = MaterialTheme.typography.labelMedium,
            color = Dusk,
        )

        Spacer(Modifier.height(50.dp))

        // 3×3 sound grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sounds) { index, sound ->
                SoundButton(
                    sound = sound,
                    isSelected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {}, colors = ButtonColors(
                containerColor = IndigoDark,
                contentColor = MoonWarm,
                disabledContainerColor = Moon,
                disabledContentColor = Indigo
            )
        ) {
            Text("Blend sounds", fontSize = 12.sp)
        }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun SoundButton(
    sound: Sound,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(14.dp)
    val activeBrush = Brush.linearGradient(
        colors = listOf(LavenderPale, AuroraPale),
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(shape)
            .then(
                if (isSelected) Modifier.background(activeBrush)
                else Modifier.background(SoundBtnBg)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Lavender.copy(alpha = 0.35f) else SoundBtnBorder,
                shape = shape,
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                sound.emoji,
                fontSize = 22.sp,
            )
            Spacer(Modifier.height(5.dp))
            Text(
                sound.name.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) Lavender else MoonGlow.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
