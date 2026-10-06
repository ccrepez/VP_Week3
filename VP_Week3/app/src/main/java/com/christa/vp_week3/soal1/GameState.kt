package com.christa.vp_week3.soal1

enum class GameState {
    START,
    WAITING,
    READY,
    TRIAL_RESULT,
    FAIL,
    FINAL
}

enum class Category(val message: String) {
    FAST("DANG YOU ARE SO FAST BRO!"),
    GOOD("YOUR REFLEX IS GOOD"),
    NORMAL("MEH LIKE OTHER PERSON"),
    SLOW("YOU LIKE A SNAIL BRO")
}

fun getCategory(avg: Int): Category = when {
    avg < 180 -> Category.FAST
    avg < 280 -> Category.GOOD
    avg < 450 -> Category.NORMAL
    else -> Category.SLOW
}