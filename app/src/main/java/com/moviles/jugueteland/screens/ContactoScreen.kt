package com.moviles.jugueteland.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.moviles.jugueteland.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(navController: NavHostController) {
    // Estados del formulario interactivo
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contáctanos", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. BANNER DE BIENVENIDA INFORMATIVO
            item {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1545558014-868c57fdeebc?q=80&w=600"),
                        contentDescription = "Mueble Juguetes", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("¡Estamos Aquí Para Ayudarte!", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Cuéntanos tu consulta y te responderemos lo más pronto posible.", color = Color.White, fontSize = 12.sp, textAlign = TextAlign.Center)
                    }
                }
            }

            // 2. FORMULARIO INTERACTIVO (Caja Blanca Flotante de Figma)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Escríbenos un mensaje", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))

                        FormCampo("Nombre y apellido", nombre) { nombre = it }
                        FormCampo("Email", email) { email = it }
                        FormCampo("Teléfono", telefono) { telefono = it }
                        FormCampo("Asunto", asunto) { asunto = it }
                        FormCampo("Mensaje", mensaje, esGrande = true) { mensaje = it }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* Acción enviar */ },
                            modifier = Modifier.fillMaxWidth().height(44.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Enviar Mensaje", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                }
            }

            // 3. BLOQUE DE ATENCIÓN DIRECTA (Tarjetas de Figma)
            item {
                Column(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Canales de Atención", color = DeepNavy, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CanalCard("📞 Teléfono", "+51 987 654 321", Modifier.weight(1f))
                        CanalCard("✉️ Correo", "hola@jugueland.pe", Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CanalCard("💬 WhatsApp", "+51 912 345 678", Modifier.weight(1f))
                        CanalCard("📍 Tienda Centro", "Barrios Altos, Lima", Modifier.weight(1f))
                    }
                }
            }

            // 4. MAPA DE UBICACIÓN (Lima - Barrios Altos de Figma)
            item {
                Column(modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 24.dp)) {
                    Text("Encuéntranos en Lima", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1524661135-423995f22d0b?q=80&w=400"), // Simula un mapa limpio urbano
                        contentDescription = "Mapa de Lima",
                        modifier = Modifier.fillMaxWidth().height(160.dp).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun FormCampo(label: String, valor: String, esGrande: Boolean = false, onValueChange: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
        Text(label, color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(if (esGrande) 100.dp else 46.dp),
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DarkTeal,
                unfocusedBorderColor = Color.LightGray,
                focusedContainerColor = Color(0xFFF9F9F9),
                unfocusedContainerColor = Color(0xFFF9F9F9)
            ),
            singleLine = !esGrande
        )
    }
}

@Composable
fun CanalCard(titulo: String, detalle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(titulo, color = CoralPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(detalle, color = DeepNavy, fontSize = 11.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}