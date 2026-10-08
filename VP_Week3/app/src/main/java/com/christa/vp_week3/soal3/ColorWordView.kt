package com.christa.vp_week3.soal3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

private const val TIME_LIMIT = 5000L
private const val MAX_WRONG = 3

@Composable
fun ColorWordView() {
    var gameState by remember {
        mutableStateOf(GameState.INITIAL)
    }

    var countdownText by remember {
        mutableStateOf("3")
    }

    var mode by remember {
        mutableStateOf(Mode.COLOR)
    }

    var word by remember {
        mutableStateOf(ColorName.RED)
    }

    var ink by remember {
        mutableStateOf(ColorName.BLUE)
    }

    var inkOnLeft by remember {
        mutableStateOf(true)
    }

    var score by remember {
        mutableStateOf(0)
    }

    var wrong by remember {
        mutableStateOf(0)
    }

    var bestScore by rememberSaveable {
        mutableStateOf(0)
    }

    var timeLeft by remember {
        mutableStateOf(TIME_LIMIT)
    }

    var questionId by remember {
        mutableStateOf(0)
    }

    fun newQuestion() {
        val colors = ColorName.entries.shuffled()
        word = colors[0]
        ink = colors[1]
        mode = Mode.entries.random()
        inkOnLeft = Random.nextBoolean()
        timeLeft = TIME_LIMIT
        questionId++
    }

    fun addWrong() {
        wrong++
        if (wrong >= MAX_WRONG) {
            if (score > bestScore) bestScore = score
            gameState = GameState.GAME_OVER
        } else {
            newQuestion()
        }
    }

    fun onAnswer(choice: Choice) {
        if (gameState != GameState.RUNNING) return
        val correct = (mode == Mode.COLOR && choice == Choice.INK) ||
                (mode == Mode.TEXT && choice == Choice.WORD)
        if (correct) {
            score++
            newQuestion()
        } else {
            addWrong()
        }
    }

    fun startGame() {
        score = 0
        wrong = 0
        gameState = GameState.COUNTDOWN
    }

    LaunchedEffect(gameState) {
        if (gameState == GameState.COUNTDOWN) {
            for (i in 3 downTo 1) {
                countdownText = "$i"
                delay(1000)
            }
            countdownText = "Start!"
            delay(700)
            newQuestion()
            gameState = GameState.RUNNING
        }
    }

    LaunchedEffect(gameState, questionId) {
        if (gameState == GameState.RUNNING) {
            val start = System.currentTimeMillis()
            while (timeLeft > 0) {
                delay(50)
                timeLeft = (TIME_LIMIT - (System.currentTimeMillis() - start)).coerceAtLeast(0)
            }
            addWrong()
        }
    }

    when (gameState) {
        GameState.INITIAL -> InitialScreen(onStart = { startGame() })

        GameState.COUNTDOWN -> CenterColumn {
            Text(countdownText, fontSize = 48.sp, fontWeight = FontWeight.Light)
        }

        GameState.RUNNING -> RunningScreen(
            mode = mode,
            score = score,
            wrong = wrong,
            timeLeft = timeLeft,
            word = word,
            ink = ink,
            inkOnLeft = inkOnLeft,
            onAnswer = { onAnswer(it) }
        )

        GameState.GAME_OVER -> GameOverScreen(
            score = score,
            bestScore = bestScore,
            onRestart = { startGame() },
            onExit = { gameState = GameState.INITIAL }
        )
    }
}

@Composable
private fun CenterColumn(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
private fun InitialScreen(onStart: () -> Unit) {
    CenterColumn {
        Text(
            "Welcome\nto\nColor Word Matching",
            fontSize = 28.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(32.dp))
        GrayButton(text = "Start Game", onClick = onStart)
    }
}

@Composable
private fun RunningScreen(
    mode: Mode,
    score: Int,
    wrong: Int,
    timeLeft: Long,
    word: ColorName,
    ink: ColorName,
    inkOnLeft: Boolean,
    onAnswer: (Choice) -> Unit
) {
    val leftChoice = if (inkOnLeft) Choice.INK else Choice.WORD
    val rightChoice = if (inkOnLeft) Choice.WORD else Choice.INK

    fun labelOf(choice: Choice): String =
        if (choice == Choice.INK) ink.label else word.label

    CenterColumn {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mode: ${mode.name}", fontSize = 14.sp)
            Text("✅ $score  ❌ $wrong/$MAX_WRONG", fontSize = 14.sp)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            mode.clue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5D4037),
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFFF3C4))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(Modifier.height(32.dp))

        Text("${(timeLeft + 999) / 1000} s", fontSize = 22.sp, fontWeight = FontWeight.Light)

        Spacer(Modifier.height(8.dp))

        Text(word.label, color = ink.color, fontSize = 56.sp, fontWeight = FontWeight.Light)

        Spacer(Modifier.height(64.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GrayButton(text = labelOf(leftChoice), onClick = { onAnswer(leftChoice) })
            GrayButton(text = labelOf(rightChoice), onClick = { onAnswer(rightChoice) })
        }
    }
}

@Composable
private fun GameOverScreen(
    score: Int,
    bestScore: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    CenterColumn {
        Text("Game Over!", fontSize = 32.sp, fontWeight = FontWeight.Light)
        Spacer(Modifier.height(24.dp))
        Text("You're Score", fontSize = 20.sp, fontWeight = FontWeight.Light)
        Text("$score", fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))
        Text("Best Score", fontSize = 12.sp)
        Text("$bestScore", fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        GrayButton(text = "Restart Game", onClick = onRestart)
        GrayButton(text = "Exit", onClick = onExit)
    }
}

@Composable
private fun GrayButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB0BEC5),
            contentColor = Color(0xFF37474F)
        )
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
private fun ColorWordPreview() {
    ColorWordView()
}