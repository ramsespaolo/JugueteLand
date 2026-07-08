package com.moviles.jugueteland.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.moviles.jugueteland.ui.theme.*

// Modelo de datos para los nuevos ingresos y preventas
data class NovedadItem(
    val titulo: String,
    val tipo: String, // "PREVENTA" o "NUEVO INGRESO"
    val fechaDisponibilidad: String,
    val precioEstimado: Double,
    val imagenUrl: String,
    val descripcion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovedadesScreen(navController: NavHostController) {

    // Lista de novedades exclusivas para el mercado peruano
    val listaNovedades = listOf(
        NovedadItem(
            titulo = "Funko Pop! Luffy Gear 5 (Edición Especial)",
            tipo = "PREVENTA",
            fechaDisponibilidad = "Llega: Julio 2026",
            precioEstimado = 89.90,
            imagenUrl = "https://images.unsplash.com/photo-1608889174633-41a0c2346e75?q=80&w=300",
            descripcion = "Reserva la transformación más esperada del año. Unidades ultra limitadas para coleccionistas."
        ),
        NovedadItem(
            titulo = "Kit de Robótica Solar STEM 4 en 1",
            tipo = "NUEVO INGRESO",
            fechaDisponibilidad = "¡Ya disponible en Almacén!",
            precioEstimado = 199.00,
            imagenUrl = "https://images.unsplash.com/photo-1532187863486-abf9d39d6618?q=80&w=300",
            descripcion = "Construye 4 modelos diferentes propulsados por energía solar. Ideal para proyectos escolares avanzados."
        ),
        NovedadItem(
            titulo = "Expansión Catan: Navegantes (Edición 2026)",
            tipo = "PREVENTA",
            fechaDisponibilidad = "Llega: 25 de Junio",
            precioEstimado = 125.00,
            imagenUrl = "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=300",
            descripcion = "Lleva tus partidas de estrategia al océano. Nueva edición con componentes mejorados de madera."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Novedades y Preventas", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = DeepNavy)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoralPrimary)
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp, 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Banner de Alertas Informativas
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SoftOrange.copy(alpha = 0.15f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Campana", tint = SoftOrange, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("¡Sé el primero en enterarte!", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("Activa las notificaciones en cada producto para separar tu pieza antes de que se agote.", color = Color.DarkGray, fontSize = 11.sp, lineHeight = 14.sp)
                        }
                    }
                }
            }

            // Renderizado de las Tarjetas de Novedades
            items(listaNovedades) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column {
                        // Imagen de cabecera de la novedad
                        Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(model = item.imagenUrl),
                                contentDescription = item.titulo,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            // Etiqueta flotante (PREVENTA / NUEVO INGRESO)
                            Box(
                                modifier = Modifier
                                    .padding(12.dp, 12.dp, 0.dp, 0.dp)
                                    .background(
                                        color = if (item.tipo == "PREVENTA") SoftOrange else DarkTeal,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp, 4.dp)
                            ) {
                                Text(text = item.tipo, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Cuerpo de información del Producto
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = item.titulo, color = DeepNavy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = item.fechaDisponibilidad, color = if (item.tipo == "PREVENTA") Color(0xFFD35400) else Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = item.descripcion, color = Color.DarkGray, fontSize = 12.sp, lineHeight = 16.sp)

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Precio Estimado", color = Color.Gray, fontSize = 10.sp)
                                    Text("S/. ${item.precioEstimado}", color = DeepNavy, fontSize = 18.sp, fontWeight = FontWeight.Black)
                                }
                                Button(
                                    onClick = { /* Lógica separar o ver */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (item.tipo == "PREVENTA") SoftOrange else DarkTeal),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(text = if (item.tipo == "PREVENTA") "Separar Cupo" else "Ver Detalles", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}