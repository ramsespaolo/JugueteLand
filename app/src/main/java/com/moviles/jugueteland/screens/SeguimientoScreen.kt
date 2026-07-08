package com.moviles.jugueteland.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.moviles.jugueteland.domain.presentation.Pantalla

import com.moviles.jugueteland.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoScreen(navController: NavHostController) {
    // Generamos un número de pedido simulado
    val pedidoId = "1042"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seguimiento de Pedido", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Pantalla.Inicio.ruta) { popUpTo(0) }
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Inicio", tint = DeepNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoralPrimary)
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(24.dp)
        ) {
            Text("Pedido #$pedidoId", fontSize = 24.sp, fontWeight = FontWeight.Black, color = DeepNavy)
            Text("Llegada estimada: En 2 a 3 días hábiles", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))

            // Construimos la línea de tiempo (Timeline)
            TimelineItem(estado = "Pedido Confirmado", descripcion = "Hemos recibido tu información.", completado = true, isLast = false)
            TimelineItem(estado = "En Preparación", descripcion = "Tu pedido está siendo empaquetado en almacén.", completado = true, isLast = false)
            TimelineItem(estado = "En Camino", descripcion = "El repartidor está en ruta hacia tu dirección.", completado = false, isLast = false)
            TimelineItem(estado = "Entregado", descripcion = "El paquete ha llegado a su destino.", completado = false, isLast = true)

            Spacer(modifier = Modifier.weight(1f))

            // Botón para volver al inicio definitivamente
            Button(
                onClick = {
                    navController.navigate(Pantalla.Inicio.ruta) { popUpTo(0) }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Home, contentDescription = "Inicio", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Volver al Inicio", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun TimelineItem(estado: String, descripcion: String, completado: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        // Columna de los puntos y la línea vertical
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(40.dp)) {
            // El circulito
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (completado) Color(0xFF4CAF50) else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (completado) {
                    Icon(Icons.Default.Check, contentDescription = "OK", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            // La línea conectora (no se dibuja si es el último paso)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(if (completado) Color(0xFF4CAF50) else Color.LightGray)
                )
            }
        }

        // Columna de los textos
        Column(modifier = Modifier.padding(start = 12.dp, bottom = 36.dp)) {
            Text(estado, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = if (completado) DeepNavy else Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(descripcion, fontSize = 12.sp, color = Color.Gray, lineHeight = 16.sp)
        }
    }
}

