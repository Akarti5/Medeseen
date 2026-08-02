package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.MedecinMainScreen
import com.example.ui.MedecinViewModel
import com.example.ui.theme.MedecinAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MedecinAppTheme {
                val viewModel: MedecinViewModel = viewModel()
                MedecinMainScreen(viewModel = viewModel)
            }
        }
    }
}
