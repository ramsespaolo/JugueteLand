package com.moviles.jugueteland.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.moviles.jugueteland.ui.theme.* // Asegúrate de importar tus colores
import com.moviles.jugueteland.ui.theme.DarkMustard
import com.moviles.jugueteland.ui.theme.DarkTeal
import com.moviles.jugueteland.ui.theme.DeepNavy
import com.moviles.jugueteland.ui.theme.SoftOrange

@Composable
fun JugueteCard(
    nombre: String,
    precio: Double,
    rating: Float,
    imagenUrl: String,
    etiqueta: String?, // Puede ser "Nuevo", "Imperdible" o null
    onComprarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. Contenedor de Imagen + Botón Favorito + Etiqueta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFFF5F5F5)) // Fondo gris suave de carga
            ) {
                // Imagen del Juguete usando Coil
                Image(
                    painter = rememberAsyncImagePainter(model = imagenUrl),
                    contentDescription = nombre,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Botón Corazón (Favoritos) arriba a la derecha
                IconButton(
                    onClick = { /* Acción de favoritos */ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Etiqueta flotante (Nuevo / Imperdible) abajo a la derecha si existe
                if (!etiqueta.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(SoftOrange, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = etiqueta,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 2. Información del Juguete (Textos y Calificación)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Fila de Estrellas de Calificación
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating.toInt()) DarkMustard else Color.LightGray,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${rating.toInt()})",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Nombre del Juguete
                Text(
                    text = nombre,
                    color = DeepNavy,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Precio en Soles (S/.)
                Text(
                    text = String.format("S/. %.2f", precio),
                    color = DeepNavy,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Botón "Comprar ahora" estilo JugueteLand
                Button(
                    onClick = onComprarClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                ) {
                    Text(
                        text = "Comprar ahora",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

