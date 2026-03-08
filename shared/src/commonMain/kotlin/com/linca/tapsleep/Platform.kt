package com.linca.tapsleep

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform