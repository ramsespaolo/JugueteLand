package com.moviles.jugueteland.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CoralPrimary,
    secondary = DarkTeal,
    background = BackgroundCream,
    surface = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = CoralPrimary,
    secondary = DarkTeal,
    tertiary = BackgroundCream,

    // Asignamos tus colores personalizados a los contenedores principales
    background = BackgroundCream, // Hará que todas tus pantallas tengan el fondo crema automático
    surface = Color.White,        // Hará que las tarjetas de juguetes sean blancas por defecto
    onBackground = DeepNavy,      // El texto sobre el fondo crema será negro azulado
    onSurface = DeepNavy          // El texto dentro de las tarjetas será negro azulado
)

@Composable
fun JugueteLandTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 🔽 CAMBIAMOS ESTO A FALSE para que use tus colores de Figma y no los del celular
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Al pasar dynamicColor a false, este bloque se ignorará y usará tus esquemas de abajo
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}