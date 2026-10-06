package com.christa.vp_week3.soal1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christa.vp_week3.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReactionView() {
    var state by remember {
        mutableStateOf(GameState.START)
    }

    var currentTrial by remember {
        mutableStateOf(0)
    }

    val results = remember {
        mutableStateListOf<Int?>(null, null, null)
    }

    var startTime by remember {
        mutableStateOf(0L)
    }

    LaunchedEffect(state, currentTrial) {
        if (state == GameState.WAITING) {
            delay(Random.nextLong(500, 4501))
            startTime = System.currentTimeMillis()
            state = GameState.READY
        }
    }

    fun onScreenClick() {
        when (state) {
            GameState.START -> state = GameState.WAITING
            GameState.WAITING -> state = GameState.FAIL
            GameState.READY -> {
                results[currentTrial] = (System.currentTimeMillis() - startTime).toInt()
                state = if (currentTrial == 2) GameState.FINAL else GameState.TRIAL_RESULT
            }

            GameState.TRIAL_RESULT -> {
                currentTrial++
                state = GameState.WAITING
            }

            GameState.FAIL -> state = GameState.START

            GameState.FINAL -> {
                for (i in results.indices) results[i] = null
                currentTrial = 0
                state = GameState.START
            }
        }
    }

    val n = currentTrial + 1

    val showCard = when (state) {
        GameState.START -> currentTrial > 0
        GameState.FAIL, GameState.TRIAL_RESULT -> true
        else -> false
    }

    when (state) {
        GameState.START -> ScreenTemplate(
            bg = Color(0xFF6ECDDD), title = "Reaction", line1 = "Test", line2 = "Click to Start",
            results = results, showCard = showCard, onClick = { onScreenClick() }
        ) { StatusIcon(Icons.Filled.FlashOn) }

        GameState.WAITING -> ScreenTemplate(
            bg = Color(0xFFD6D6D6), title = "Get Ready", line1 = "Wait for green light...",
            line2 = "DON'T CLICK YET!", results = results, showCard = showCard, onClick = { onScreenClick() }
        ) { StatusIcon(Icons.Filled.Warning) }

        GameState.READY -> ScreenTemplate(
            bg = Color(0xFF4CAF50), title = "GO!", line1 = "CLICK NOW!",
            line2 = "TAP AS FAST AS YOU CAN!", results = results, showCard = showCard, onClick = { onScreenClick() }
        ) { StatusIcon(Icons.AutoMirrored.Filled.DirectionsRun) }

        GameState.TRIAL_RESULT -> ScreenTemplate(
            bg = Color(0xFF4CAF50), title = "Trial $n Complete!",
            line1 = "Time: ${results[currentTrial]}ms", line2 = "Continue to Trial ${n + 1}",
            results = results, showCard = showCard, onClick = { onScreenClick() }
        ) { StatusIcon(Icons.Filled.CheckCircle) }

        GameState.FAIL -> ScreenTemplate(
            bg = Color(0xFFF94144), title = "FAIL!",
            line1 = "You clicked too early, TRY TO READ THE RULE BRO", line2 = "TRY AGAIN",
            results = results, showCard = showCard, onClick = { onScreenClick() }
        ) { StatusIcon(Icons.Filled.ThumbDown) }

        GameState.FINAL -> FinalScreen(results = results, onClick = { onScreenClick() })
    }
}

@Composable
private fun StatusIcon(icon: ImageVector) {
    Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(96.dp))
}

@Composable
private fun ScreenTemplate(
    bg: Color,
    title: String,
    line1: String,
    line2: String,
    results: List<Int?>,
    showAverage: Boolean = false,
    showCard: Boolean = false,
    onClick: () -> Unit,
    visual: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .clickable { onClick() }
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            title, color = Color.White, fontSize = 26.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        visual()
        Spacer(Modifier.height(24.dp))
        Text(line1, color = Color.White, fontSize = 18.sp, textAlign = TextAlign.Center)
        Text(line2, color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        if (showCard) {
            TrialResultsCard(results, showAverage)
        }
    }
}

@Composable
private fun FinalScreen(results: List<Int?>, onClick: () -> Unit) {
    val valid = results.filterNotNull()
    val avg = if (valid.isEmpty()) 9999 else valid.average().toInt()
    val cat = getCategory(avg)
    val (bg, img) = when (cat) {
        Category.FAST -> Color(0xFF00E676) to R.drawable.hormat
        Category.GOOD -> Color(0xFF2196F3) to R.drawable.jempol
        Category.NORMAL -> Color(0xFFFF9800) to R.drawable.standard
        Category.SLOW -> Color(0xFFFF5722) to R.drawable.jempol_bawah
    }
    ScreenTemplate(
        bg = bg,
        title = cat.message,
        line1 = if (valid.isEmpty()) "No valid trial" else "Average: ${avg}ms",
        line2 = "Click to Start New Test",
        results = results,
        showAverage = valid.isNotEmpty(),
        showCard = true,
        onClick = onClick
    ) {
        Image(painterResource(img), contentDescription = null, modifier = Modifier.size(140.dp))
    }
}

@Composable
private fun TrialResultsCard(results: List<Int?>, showAverage: Boolean) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Trial Results", color = Color(0xFF1565C0), fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                results.forEachIndexed { i, r ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${i + 1}",
                            color = if (r != null) Color(0xFF4CAF50) else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Text(if (r != null) "${r}ms" else "-", fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
            if (showAverage) {
                val avg = results.filterNotNull().average().toInt()
                Text("Average Score", color = Color(0xFF1565C0), fontSize = 12.sp)
                Text(
                    "${avg}ms", color = Color(0xFFE65100),
                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReactionPreview() {
    ReactionView()
}