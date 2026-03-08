package com.linca.tapsleep.ui

enum class TimerPreset(val totalSeconds: Int?, val label: String) {
    SEC_5(5, "5s"),
    MIN_15(15 * 60, "15m"),
    MIN_30(30 * 60, "30m"),
    MIN_45(45 * 60, "45m"),
    MIN_60(60 * 60, "1h"),
    HR_8(8 * 60 * 60, "8h"),
    NONE(null, "∞"),
}
