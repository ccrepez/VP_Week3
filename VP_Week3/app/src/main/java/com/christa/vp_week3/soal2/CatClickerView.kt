package com.christa.vp_week3.soal2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.christa.vp_week3.R
import kotlinx.coroutines.delay
import kotlin.math.ceil

@Composable
fun CatClickerView() {
    var coins by remember {
        mutableStateOf(0)
    }

    var coinPerTap by remember {
        mutableStateOf(1)
    }

    var upgradeCost by remember {
        mutableStateOf(10)
    }

    var isHolding by remember {
        mutableStateOf(false)
    }

    var tapCount by remember {
        mutableStateOf(0)
    }

    var mouthOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(isHolding, tapCount) {
        if (!isHolding) {
            delay(200)
            mouthOpen = false
        }
    }

    val nextValue = ceil(coinPerTap * 1.5).toInt()
    val canUpgrade = coins >= upgradeCost

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.cat_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            CoinCard(coins = coins, coinPerTap = coinPerTap)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Tap the Cat!",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = painterResource(
                        if (mouthOpen) R.drawable.cat_open else R.drawable.cat_close
                    ),
                    contentDescription = "Cat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    isHolding = true
                                    mouthOpen = true
                                    tapCount++
                                    coins += coinPerTap
                                    tryAwaitRelease()
                                    isHolding = false
                                }
                            )
                        }
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    if (mouthOpen) "Meow!" else "Purr~",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            UpgradeCard(
                nextValue = nextValue,
                upgradeCost = upgradeCost,
                coins = coins,
                canUpgrade = canUpgrade,
                onUpgrade = {
                    coins -= upgradeCost
                    coinPerTap = nextValue
                    upgradeCost *= 2
                }
            )
        }
    }
}

@Composable
private fun CoinCard(coins: Int, coinPerTap: Int) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.3f))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Your Coins", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("$coins", color = Color(0xFF00E676), fontSize = 40.sp, fontWeight = FontWeight.Bold)
        Text("$coinPerTap coins per tap", color = Color.White, fontSize = 12.sp)
    }
}

@Composable
private fun UpgradeCard(
    nextValue: Int,
    upgradeCost: Int,
    coins: Int,
    canUpgrade: Boolean,
    onUpgrade: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Give Me Your Coin",
            color = Color(0xFF444444),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Next upgrade: +$nextValue coins per tap",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onUpgrade,
            enabled = canUpgrade,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                if (canUpgrade) "Pay for $upgradeCost coins"
                else "Find ${upgradeCost - coins} more coins",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CatClickerPreview() {
    CatClickerView()
}