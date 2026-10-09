package com.christa.vp_week3.soal4

enum class Rps(val emoji: String, val label: String) {
    ROCK("✊", "Rock"),
    PAPER("✋", "Paper"),
    SCISSOR("✌️", "Scissor");

    fun beats(other: Rps): Boolean =
        (this == ROCK && other == SCISSOR) ||
                (this == PAPER && other == ROCK) ||
                (this == SCISSOR && other == PAPER)
}

enum class State { INITIAL, PICK, REVEAL, FINISHED }

enum class Result(val text: String) {
    WIN("You Win!"),
    LOSE("You Lose"),
    DRAW("Draw")
}

fun judge(player: Rps, cpu: Rps): Result = when {
    player == cpu -> Result.DRAW
    player.beats(cpu) -> Result.WIN
    else -> Result.LOSE
}