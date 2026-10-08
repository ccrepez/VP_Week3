package com.christa.vp_week3.soal3

import androidx.compose.ui.graphics.Color

enum class GameState {
    INITIAL,
    COUNTDOWN,
    RUNNING,
    GAME_OVER
}

enum class Mode(val clue: String) {
    COLOR("Pilih WARNA TINTA"),
    TEXT("Pilih NAMA KATA")
}

enum class Choice { INK, WORD }

enum class ColorName(val label: String, val color: Color) {
    RED("RED", Color(0xFFE53935)),
    BLUE("BLUE", Color(0xFF1E88E5)),
    GREEN("GREEN", Color(0xFF43A047)),
    ORANGE("ORANGE", Color(0xFFFB8C00)),
    PURPLE("PURPLE", Color(0xFF8E24AA))
}