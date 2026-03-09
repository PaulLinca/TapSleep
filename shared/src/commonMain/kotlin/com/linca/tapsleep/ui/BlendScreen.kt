package com.linca.tapsleep.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.linca.tapsleep.ui.theme.AuroraPale
import com.linca.tapsleep.ui.theme.Dusk
import com.linca.tapsleep.ui.theme.Lavender
import com.linca.tapsleep.ui.theme.LavenderPale
import com.linca.tapsleep.ui.theme.Deep
import com.linca.tapsleep.ui.theme.Moon
import com.linca.tapsleep.ui.theme.MoonGlow
import com.linca.tapsleep.ui.theme.Night

private val SlotBg = Deep.copy(alpha = 0.6f)
private val SlotBorder = MoonGlow.copy(alpha = 0.4f)

@Composable
fun BlendScreen(
    onPlay: (List<Sound>) -> Unit,
    onBack: () -> Unit,
) {
    // Up to 3 slots, each holds a Sound or null
    var slots by remember { mutableStateOf(listOf<Sound?>(null, null, null)) }
    val selectedSounds = slots.filterNotNull()
    val canPlay = selectedSounds.size >= 2

    fun toggle(sound: Sound) {
        val idx = slots.indexOf(sound)
        if (idx >= 0) {
            // already in a slot — remove it
            slots = slots.toMutableList().also { it[idx] = null }
        } else {
            // add to first empty slot (if any)
            val emptyIdx = slots.indexOf(null)
            if (emptyIdx >= 0) {
                slots = slots.toMutableList().also { it[emptyIdx] = sound }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        StarfieldBackground(Modifier.fillMaxSize())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp),
    ) {
        Spacer(Modifier.height(20.dp))

        // ── Top bar ───────────────────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth()) {
            BackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))
        }

        Spacer(Modifier.height(24.dp))

        // ── Title ─────────────────────────────────────────────────────────────
        Text(
            "Your Mix.",
            style = MaterialTheme.typography.headlineLarge,
            color = MoonGlow,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "BLEND UP TO 3 SOUNDS",
            style = MaterialTheme.typography.labelLarge,
            color = Dusk,
        )

        Spacer(Modifier.height(32.dp))

        // ── 3 mix slots ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            slots.forEachIndexed { index, sound ->
                MixSlot(
                    sound = sound,
                    modifier = Modifier.weight(1f),
                    onRemove = {
                        slots = slots.toMutableList().also { it[index] = null }
                    },
                )
            }
        }

        Spacer(Modifier.height(40.dp))

        Text(
            "SOUNDS",
            style = MaterialTheme.typography.labelLarge,
            color = Dusk,
        )

        Spacer(Modifier.height(12.dp))

        // ── Sound picker grid ─────────────────────────────────────────────────
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            sounds.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    row.forEach { sound ->
                        val isSelected = sound in slots
                        val isFull = slots.none { it == null } && !isSelected
                        BlendSoundButton(
                            sound = sound,
                            isSelected = isSelected,
                            enabled = !isFull,
                            onClick = { toggle(sound) },
                            modifier = Modifier.weight(1f).fillMaxSize(),
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Play button ───────────────────────────────────────────────────────
        val playScale by animateFloatAsState(
            targetValue = if (canPlay) 1f else 0.96f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
            label = "playScale",
        )
        val playBrush = Brush.linearGradient(colors = listOf(LavenderPale, AuroraPale))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .graphicsLayer { scaleX = playScale; scaleY = playScale }
                .clip(RoundedCornerShape(25.dp))
                .drawBehind {
                    drawRect(SlotBg)
                    if (canPlay) drawRect(playBrush, alpha = 0.6f)
                }
                .border(
                    1.dp,
                    if (canPlay) Lavender.copy(alpha = 0.55f) else SlotBorder,
                    RoundedCornerShape(25.dp),
                )
                .clickable(enabled = canPlay) { onPlay(selectedSounds) },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "Play Mix",
                style = MaterialTheme.typography.labelLarge,
                color = if (canPlay) Moon else MoonGlow.copy(alpha = 0.35f),
            )
        }

        Spacer(Modifier.height(30.dp))
    } // Column
    } // outer Box
}

@Composable
private fun MixSlot(
    sound: Sound?,
    modifier: Modifier = Modifier,
    onRemove: () -> Unit,
) {
    val shape = RoundedCornerShape(20.dp)
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(shape)
            .background(SlotBg)
            .border(1.dp, if (sound != null) sound.tint.copy(alpha = 0.4f) else SlotBorder, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (sound == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add sound",
                    tint = MoonGlow.copy(alpha = 0.3f),
                    modifier = Modifier.size(22.dp),
                )
                Text(
                    "ADD SOUND",
                    style = MaterialTheme.typography.labelSmall,
                    color = MoonGlow.copy(alpha = 0.25f),
                )
            }
        } else {
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
                    style = MaterialTheme.typography.labelSmall,
                    color = MoonGlow,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            // Remove button — top-right corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Night.copy(alpha = 0.6f))
                    .clickable(onClick = onRemove),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Remove",
                    tint = MoonGlow.copy(alpha = 0.6f),
                    modifier = Modifier.size(11.dp),
                )
            }
        }
    }
}

@Composable
private fun BlendSoundButton(
    sound: Sound,
    isSelected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(25.dp)
    val activeBrush = Brush.linearGradient(colors = listOf(LavenderPale, AuroraPale))

    val glowProgress by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "glow",
    )
    val alpha = if (enabled || isSelected) 1f else 0.35f

    Box(
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .clip(shape)
            .drawBehind {
                drawRect(SlotBg)
                drawRect(activeBrush, alpha = glowProgress)
            }
            .border(
                width = 1.dp,
                color = if (isSelected) Lavender.copy(alpha = 0.55f) else SlotBorder,
                shape = shape,
            )
            .clickable(enabled = enabled || isSelected, onClick = onClick),
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
                color = if (isSelected) sound.tint else MoonGlow.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
