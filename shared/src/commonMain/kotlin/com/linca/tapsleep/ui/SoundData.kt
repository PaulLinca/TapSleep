package com.linca.tapsleep.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.BlurOn
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material.icons.rounded.Park
import androidx.compose.material.icons.rounded.Thunderstorm
import androidx.compose.material.icons.rounded.Grain
import androidx.compose.material.icons.rounded.WaterDrop
import androidx.compose.material.icons.rounded.Waves
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Sound(val name: String, val icon: ImageVector, val tint: Color)

internal val sounds = listOf(
    Sound("Rain",    Icons.Rounded.WaterDrop,          Color(0xFF7EB8C4)), // Aurora — water blue
    Sound("Ocean",   Icons.Rounded.Waves,              Color(0xFF5B8FA8)), // deep ocean
    Sound("Forest",  Icons.Rounded.Park,               Color(0xFF8FA89A)), // muted green
    Sound("Fire",    Icons.Rounded.LocalFireDepartment,Color(0xFFC4956A)), // muted amber
    Sound("Wind",    Icons.Rounded.Air,                Color(0xFFA8B4C8)), // silver-blue
    Sound("Thunder", Icons.Rounded.Thunderstorm,       Color(0xFF8B9EC4)), // stormy blue-grey
    Sound("Pink",    Icons.Rounded.GraphicEq,          Color(0xFFc28fc4)), // soft lavender-pink
    Sound("Brown",   Icons.Rounded.Grain,          Color(0xFFa89476)), // sandy warm
    Sound("White",   Icons.Rounded.BlurOn,             Color(0xFFE8E4D8)), // Moon — pale white
)
