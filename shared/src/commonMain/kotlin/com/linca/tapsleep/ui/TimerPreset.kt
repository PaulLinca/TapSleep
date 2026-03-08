package com.linca.tapsleep.ui

enum class TimerPreset(val totalSeconds: Int?, val label: String) {
    MIN_15(15 * 60, "15"),
    MIN_30(30 * 60, "30"),
    MIN_45(45 * 60, "45"),
    MIN_60(60 * 60, "60"),
    NONE(null, "∞"),
}
