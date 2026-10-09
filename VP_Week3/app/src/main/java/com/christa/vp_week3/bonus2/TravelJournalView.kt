package com.christa.vp_week3.bonus2

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Background = Color(0xFF0F1626)
private val FieldColor = Color(0xFF1C2537)
private val BorderColor = Color(0xFF2E3A52)
private val Accent = Color(0xFF5DB8F5)
private val LabelColor = Color(0xFF9AA8C0)
private val HintColor = Color(0xFF5E6B82)
private val DarkText = Color(0xFF0F1626)

enum class Satisfaction(val label: String) {
    BIASA("Biasa"),
    SERU("Seru"),
    LUAR_BIASA("Luar Biasa ★")
}

@Composable
fun TravelJournalView() {
    val context = LocalContext.current

    var spot by remember {
        mutableStateOf("")
    }

    var experience by remember {
        mutableStateOf("")
    }

    var notes by remember {
        mutableStateOf("")
    }

    var satisfaction by remember {
        mutableStateOf(Satisfaction.LUAR_BIASA)
    }

    var isSaved by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding()
            .imePadding()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Banner()

            JournalField(
                label = "SPOT WISATA FAVORIT",
                value = spot,
                onValueChange = {
                    spot = it
                    isSaved = false
                },
                placeholder = "Contoh: Fjellheisen Cable Car",
                singleLine = true
            )

            JournalField(
                label = "APA YANG PALING KAMU NIKMATI?",
                value = experience,
                onValueChange = {
                    experience = it
                    isSaved = false
                },
                placeholder = "Ceritakan pengalamanmu...",
                minLines = 3
            )

            JournalField(
                label = "CATATAN TAMBAHAN / PERLENGKAPAN",
                value = notes,
                onValueChange = {
                    notes = it
                    isSaved = false
                },
                placeholder = "(Ketik perlengkapan ekstra di sini...)"
            )

            Text(
                "TINGKAT KEPUASAN:",
                color = LabelColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Satisfaction.entries.forEach { option ->
                    SatisfactionChip(
                        label = option.label,
                        selected = satisfaction == option,
                        onClick = {
                            satisfaction = option
                            isSaved = false
                        }
                    )
                }
            }
        }

        Text(
            buildAnnotatedString {
                append("Status: ")
                withStyle(SpanStyle(color = Accent, fontWeight = FontWeight.Bold)) {
                    append(if (isSaved) "Draft Tersimpan" else "Belum Disimpan")
                }
            },
            color = LabelColor,
            fontSize = 13.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 18.dp)
        )

        FloatingActionButton(
            onClick = {
                if (spot.isBlank()) {
                    Toast.makeText(
                        context,
                        "Isi dulu spot wisata favoritmu ya!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    isSaved = true
                    Toast.makeText(
                        context,
                        "Jurnal \"${spot.trim()}\" berhasil disimpan! (${satisfaction.label})",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = Accent,
            contentColor = DarkText,
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Simpan jurnal")
        }
    }
}

@Composable
private fun Banner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF3B82C4)))
            )
            .padding(16.dp)
    ) {
        Text("LOG PERJALANAN", color = Color(0xFFBFD7F5), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text("Tromsø, Norway ❄️", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Ekspedisi Aurora Borealis", color = Color(0xFFBFD7F5), fontSize = 12.sp)
    }
}

@Composable
private fun JournalField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean = false,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
        placeholder = { Text(placeholder) },
        singleLine = singleLine,
        minLines = minLines,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FieldColor,
            unfocusedContainerColor = FieldColor,
            focusedBorderColor = Accent,
            unfocusedBorderColor = BorderColor,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Accent,
            unfocusedLabelColor = LabelColor,
            focusedPlaceholderColor = HintColor,
            unfocusedPlaceholderColor = HintColor,
            cursorColor = Accent
        )
    )
}

@Composable
private fun SatisfactionChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(if (selected) Accent else Color.Transparent)
            .border(1.dp, if (selected) Accent else BorderColor, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            label,
            fontSize = 13.sp,
            color = if (selected) DarkText else LabelColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TravelJournalPreview() {
    TravelJournalView()
}