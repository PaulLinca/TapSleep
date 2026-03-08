package com.linca.tapsleep.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bedtime
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.LocalCafe
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Sound(val name: String, val icon: ImageVector, val tint: Color)

internal val sounds = listOf(
    Sound("Rain",   Icons.Rounded.WaterDrop,           Color(0xFF7EB8C4)), // Aurora — water blue
    Sound("Ocean",  Icons.Rounded.Waves,               Color(0xFF5B8FA8)), // deep ocean
    Sound("Forest", Icons.Rounded.Park,                Color(0xFF8FA89A)), // Sage — muted green
    Sound("Fire",   Icons.Rounded.LocalFireDepartment, Color(0xFFC4956A)), // muted amber
    Sound("Wind",   Icons.Rounded.Air,                 Color(0xFFA8B4C8)), // silver-blue
    Sound("Café",   Icons.Rounded.LocalCafe,           Color(0xFFB89A78)), // warm coffee
    Sound("Space",  Icons.Rounded.AutoAwesome,         Color(0xFF9B8FC4)), // Lavender — cosmic
    Sound("Brown",  Icons.Rounded.Grain,               Color(0xFFA89880)), // sandy warm
    Sound("Night",  Icons.Rounded.Bedtime,             Color(0xFF6B7DB3)), // Dusk — deep blue
)
