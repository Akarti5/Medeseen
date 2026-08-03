package com.medseen.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.medseen.app.ui.MedecinMainScreen
import com.medseen.app.ui.MedecinViewModel
import com.medseen.app.ui.theme.MedecinAppTheme

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
