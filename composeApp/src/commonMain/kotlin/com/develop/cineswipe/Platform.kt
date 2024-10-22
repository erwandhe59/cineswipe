package com.develop.cineswipe

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform