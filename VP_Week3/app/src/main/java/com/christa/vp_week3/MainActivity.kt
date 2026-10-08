package com.christa.vp_week3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
//ubah ke package soal berapa

//import com.christa.vp_week3.soal1.ReactionView
//import com.christa.vp_week3.soal2.CatClickerView
import com.christa.vp_week3.soal3.ColorWordView

import com.christa.vp_week3.ui.theme.VP_Week3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VP_Week3Theme {
                //ubah kalau mau run soal berapa

                //ReactionView() //soal1
                //CatClickerView() //soal2
                ColorWordView() //soal3
            }
        }
    }
}