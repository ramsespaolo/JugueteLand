package com.moviles.jugueteland

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.moviles.jugueteland.domain.presentation.GrafoNavegacionJugueteLand
import com.moviles.jugueteland.ui.theme.JugueteLandTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JugueteLandTheme {
                // Creamos el NavController real de la App
                val navController = rememberNavController()

                // 2. Llamamos a la nueva función en español
                GrafoNavegacionJugueteLand(navController = navController)
            }
        }
    }
}