package com.christa.vp_week3.soal4

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private const val BEST_OF = 5

@Composable
fun RpsView() {
    val target = BEST_OF / 2 + 1

    var state by rememberSaveable {
        mutableStateOf(State.INITIAL)
    }

    var yourScore by rememberSaveable {
        mutableStateOf(0)
    }

    var cpuScore by rememberSaveable {
        mutableStateOf(0)
    }

    var bestScore by rememberSaveable {
        mutableStateOf(0)
    }

    var playerPick by remember {
        mutableStateOf<Rps?>(null)
    }

    var cpuPick by remember {
        mutableStateOf<Rps?>(null)
    }

    var result by remember {
        mutableStateOf<Result?>(null)
    }

    var buttonOrder by remember {
        mutableStateOf(Rps.entries.shuffled())
    }

    fun newRound() {
        playerPick = null
        cpuPick = null
        result = null
        buttonOrder = Rps.entries.shuffled()
        state = State.PICK
    }

    fun resetMatch() {
        yourScore = 0
        cpuScore = 0
    }

    fun onPick(move: Rps) {
        if (state != State.PICK) return
        val cpu = Rps.entries.random()
        val r = judge(move, cpu)
        playerPick = move
        cpuPick = cpu
        result = r
        when (r) {
            Result.WIN -> yourScore++
            Result.LOSE -> cpuScore++
            Result.DRAW -> {}
        }
        state = State.REVEAL
    }

    LaunchedEffect(state) {
        if (state == State.REVEAL) {
            delay(700)
            if (yourScore >= target || cpuScore >= target) {
                val diff = yourScore - cpuScore
                if (diff > bestScore) bestScore = diff
                state = State.FINISHED
            } else {
                newRound()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ScoreHeader(yourScore = yourScore, cpuScore = cpuScore)

        Spacer(Modifier.height(48.dp))

        when (state) {
            State.INITIAL -> {
                Text("Rock • Paper • Scissors", fontSize = 22.sp, fontWeight = FontWeight.Light)
                Spacer(Modifier.height(24.dp))
                GrayButton(
                    text = "Start",
                    description = "Start match",
                    modifier = Modifier.width(160.dp),
                    onClick = {
                        resetMatch()
                        newRound()
                    }
                )
            }

            State.PICK -> {
                Text("Pick your move!", fontSize = 14.sp)
                Spacer(Modifier.height(8.dp))
                VsRow(left = "❔", right = "❔")
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    buttonOrder.forEach { move ->
                        GrayButton(
                            text = "${move.emoji} ${move.label}",
                            description = "Pick ${move.label}",
                            onClick = { onPick(move) }
                        )
                    }
                }
            }

            State.REVEAL -> {
                VsRow(left = playerPick?.emoji ?: "❔", right = cpuPick?.emoji ?: "❔")
                Text(
                    result?.text ?: "",
                    fontSize = 16.sp,
                    modifier = Modifier.semantics {
                        contentDescription = "Round result: ${result?.text ?: ""}"
                    }
                )
            }

            State.FINISHED -> {
                val matchText =
                    if (yourScore > cpuScore) "You Win the Match!" else "You Lose the Match"
                Text(
                    matchText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.semantics { contentDescription = matchText }
                )
                Text("Best Score: $bestScore", fontSize = 12.sp)
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GrayButton(
                        text = "Restart",
                        description = "Restart match",
                        onClick = {
                            resetMatch()
                            newRound()
                        }
                    )
                    GrayButton(
                        text = "Exit",
                        description = "Exit to start screen",
                        onClick = {
                            resetMatch()
                            state = State.INITIAL
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreHeader(yourScore: Int, cpuScore: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "🧑 $yourScore — $cpuScore 🤖",
            fontSize = 14.sp,
            modifier = Modifier.semantics {
                contentDescription = "Score: you $yourScore, computer $cpuScore"
            }
        )
        Text("Best of $BEST_OF", fontSize = 12.sp, color = Color.Gray)
    }
}

@Composable
private fun VsRow(left: String, right: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(left, fontSize = 40.sp)
        Text("  VS  ", fontSize = 32.sp, fontWeight = FontWeight.Light)
        Text(right, fontSize = 40.sp)
    }
}

@Composable
private fun GrayButton(
    text: String,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.semantics { contentDescription = description },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB0BEC5),
            contentColor = Color(0xFF37474F)
        )
    ) {
        Text(text, fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
private fun RpsPreview() {
    RpsView()
}