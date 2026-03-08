package com.linca.tapsleep.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.animation.animateColorAsState
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.linca.tapsleep.ui.theme.AuroraPale
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Lavender
import com.linca.tapsleep.ui.theme.LavenderPale
import com.linca.tapsleep.ui.theme.MoonGlow
import com.linca.tapsleep.ui.theme.Night

private val SoundBtnBg = Color(0x0DC8C0A8)
private val SoundBtnBorder = MoonGlow.copy(alpha = 0.5f)
private val SoundBtnText = MoonGlow.copy(alpha = 0.5f)

@Composable
fun MainScreen(onSoundClick: (Sound) -> Unit = {}, onBlendClick: () -> Unit = {}) {
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Night)
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
    ) {

        Spacer(Modifier.height(30.dp))

        TapSleepLogo(Modifier.align(alignment = Alignment.CenterHorizontally), fontSize = 24)

        Spacer(Modifier.weight(0.5f))

        Text(
            "Tonight.",
            style = MaterialTheme.typography.headlineLarge,
            color = MoonGlow,
        )

        Spacer(Modifier.height(10.dp))

        Text(
            "CHOOSE A SOUND",
            style = MaterialTheme.typography.labelLarge,
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
                    onClick = {
                        selectedIndex = index
                        onSoundClick(sound)
                    },
                )
            }
        }

        Spacer(Modifier.height(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(SoundBtnBg)
                .border(1.dp, MoonGlow.copy(alpha = 0.5f), RoundedCornerShape(25.dp))
                .clickable(onClick = onBlendClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Blend sounds",
                style = MaterialTheme.typography.labelLarge,
                color = MoonGlow,
            )
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
    val shape = RoundedCornerShape(25.dp)
    val activeBrush = Brush.linearGradient(colors = listOf(LavenderPale, AuroraPale))

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Press: shrink slightly; release: spring back
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.91f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "scale",
    )

    // 0 → 1 as the button becomes selected, drives gradient + border glow
    val glowProgress by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "glow",
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Lavender.copy(alpha = 0.55f) else SoundBtnBorder,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "border",
    )
    val labelColor by animateColorAsState(
        targetValue = if (isSelected) sound.tint else SoundBtnText,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "label",
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .drawBehind {
                drawRect(SoundBtnBg)
                drawRect(activeBrush, alpha = glowProgress)
            }
            .border(width = 1.dp, color = borderColor, shape = shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Icon(
                imageVector = sound.icon,
                contentDescription = sound.name,
                tint = sound.tint,
                modifier = Modifier.size(26.dp),
            )
            Text(
                sound.name.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = labelColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
