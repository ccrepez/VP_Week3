package com.christa.vp_week3.bonus1

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

private val Brown = Color(0xFF4E2A1E)
private val BrownLight = Color(0xFF8B4A2B)
private val Orange = Color(0xFFE0662B)
private val Cream = Color(0xFFF8F3EF)
private val Peach = Color(0xFFFDF0E3)
private val PeachBorder = Color(0xFFF5D9C0)

enum class CoffeeSize(val label: String, val extraPrice: Int) {
    REGULAR("Regular (+0)", 0),
    LARGE("Large (+6k)", 6000)
}

private const val BASE_PRICE = 25000

fun rupiah(value: Int): String =
    "Rp " + NumberFormat.getNumberInstance(Locale.forLanguageTag("id-ID")).format(value)

@Composable
fun CoffeeOrderView() {
    val context = LocalContext.current

    var quantity by remember {
        mutableStateOf(1)
    }

    var size by remember {
        mutableStateOf(CoffeeSize.REGULAR)
    }

    val unitPrice = BASE_PRICE + size.extraPrice
    val subtotal = unitPrice * quantity
    val tax = subtotal / 10
    val total = subtotal + tax

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .systemBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Kopi Kenangan Senja",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Brown
            )
            Spacer(Modifier.weight(1f))
            Text("☕", fontSize = 22.sp)
        }

        Spacer(Modifier.height(12.dp))

        ProductCard(unitPrice = unitPrice)

        Spacer(Modifier.height(16.dp))

        Text("PILIHAN UKURAN:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Brown)
        Spacer(Modifier.height(6.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CoffeeSize.entries.forEach { option ->
                SizeOption(
                    label = option.label,
                    selected = size == option,
                    onClick = { size = option },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        QuantityCard(
            quantity = quantity,
            onMinus = { if (quantity > 1) quantity-- },
            onPlus = { quantity++ }
        )

        Spacer(Modifier.height(12.dp))

        SummaryBox(subtotal = subtotal, tax = tax, total = total)

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Caramel Latte ${size.name.lowercase()} x$quantity ditambahkan ke keranjang (${rupiah(total)})",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Orange)
        ) {
            Text("TAMBAH KE KERANJANG", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ProductCard(unitPrice: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PeachBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF6D3A1F), Color(0xFF8B3A1A)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Outlined.LocalCafe,
                    contentDescription = "Coffee",
                    tint = Color(0xFFF5D9A8),
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text("Caramel Latte", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Brown)
            Text(
                "Espresso shot, steamed milk & caramel syrup",
                fontSize = 12.sp,
                color = BrownLight
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "${rupiah(unitPrice)} / cup",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Orange
            )
        }
    }
}

@Composable
private fun SizeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) Color.White else Peach)
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) Orange else PeachBorder,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            fontSize = 13.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Orange else BrownLight
        )
    }
}

@Composable
private fun QuantityCard(quantity: Int, onMinus: () -> Unit, onPlus: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, PeachBorder, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Jumlah Pesanan:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Brown)
            Spacer(Modifier.weight(1f))
            QtyButton(
                symbol = "-",
                enabled = quantity > 1,
                background = Color.White,
                onClick = onMinus
            )
            Text(
                "$quantity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Brown,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
            QtyButton(
                symbol = "+",
                enabled = true,
                background = Color(0xFFFDE7D3),
                onClick = onPlus
            )
        }
    }
}

@Composable
private fun QtyButton(
    symbol: String,
    enabled: Boolean,
    background: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .border(1.dp, Color(0xFFD5D8DE), RoundedCornerShape(8.dp))
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            symbol,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (!enabled) Color.LightGray else if (symbol == "+") Orange else Brown
        )
    }
}

@Composable
private fun SummaryBox(subtotal: Int, tax: Int, total: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFFDF6EE))
            .drawBehind {
                drawRoundRect(
                    color = PeachBorder,
                    style = Stroke(
                        width = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
                    ),
                    cornerRadius = CornerRadius(12.dp.toPx())
                )
            }
            .padding(12.dp)
    ) {
        Column {
            SummaryRow("Subtotal:", rupiah(subtotal))
            SummaryRow("Pajak Resto (10%):", rupiah(tax))
            Spacer(Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(PeachBorder)
            )
            Spacer(Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Total Tagihan:", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Brown)
                Spacer(Modifier.weight(1f))
                Text(rupiah(total), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Orange)
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, color = BrownLight)
        Spacer(Modifier.weight(1f))
        Text(value, fontSize = 12.sp, color = Brown)
    }
}

@Preview(showBackground = true)
@Composable
private fun CoffeeOrderPreview() {
    CoffeeOrderView()
}