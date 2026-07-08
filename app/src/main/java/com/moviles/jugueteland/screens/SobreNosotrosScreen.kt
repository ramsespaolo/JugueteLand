package com.moviles.jugueteland.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun SobreNosotrosScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiénes Somos", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
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
            // 1. HERO BANNER PRINCIPAL
            item {
                Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1515488042361-404e9250afef?q=80&w=600"),
                        contentDescription = "Fondo Juguetes",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp, 0.dp, 24.dp, 0.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Quiénes somos", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Nos comprometemos en crear momentos felices, ofreciendo juguetes que inspiran aprendizaje y diversión",
                            color = Color.White, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 18.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { navController.navigate("Novedades") },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text("Nuestros novedades", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 2. SECCIÓN: DONDE LA DIVERSIÓN INSPIRA SUEÑOS
            item {
                Column(modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 16.dp)) {
                    Box(
                        modifier = Modifier.background(Color(0xFFC3E2C2), RoundedCornerShape(12.dp)).padding(12.dp, 6.dp)
                    ) {
                        Text("✨ Donde la Diversión Inspira Sueños", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Nacimos para ofrecer juguetes de calidad que mezclan diversión y aprendizaje. Queremos que cada niño descubra, imagine y viva nuevas aventuras con productos pensados para su crecimiento.\n\nTrabajamos con marcas confiables y materiales seguros para cada etapa. Nuestro objetivo es brindar experiencias que acompañan a los niños mientras crecen y exploran.",
                            color = Color.DarkGray, fontSize = 12.sp, lineHeight = 16.sp, modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Image(
                            painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1596464716127-f2a82984de30?q=80&w=300"),
                            contentDescription = "Niños Jugando",
                            modifier = Modifier.size(110.dp).clip(RoundedCornerShape(0.dp, 40.dp, 0.dp, 40.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            // 3. SECCIÓN: NUESTROS VALORES (Grilla Asimétrica Manual de Figma)
            item {
                Column(modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.background(Color(0xFFFFC4C4), RoundedCornerShape(12.dp)).padding(16.dp, 6.dp)) {
                        Text("✨ Nuestros valores", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Fila 1: Seguridad y Foto Bebé
                    Row(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFFFE5CA), RoundedCornerShape(12.dp, 0.dp, 0.dp, 0.dp)), contentAlignment = Alignment.Center) {
                            Text("Seguridad", color = Color(0xFFD35400), fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Image(
                            painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1559251606-c623743a6d76?q=80&w=300"),
                            contentDescription = "Seguridad", modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(0.dp, 12.dp, 0.dp, 0.dp)), contentScale = ContentScale.Crop
                        )
                    }
                    // Fila 2: Calidad y Creatividad
                    Row(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFF0F4F8)), contentAlignment = Alignment.Center) {
                            Text("Calidad", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFFFC4C4).copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                            Text("Creatividad", color = CoralPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                    // Fila 3: Foto Niños Mesa y Aprendizaje
                    Row(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1513364776144-60967b0f800f?q=80&w=300"),
                            contentDescription = "Mesa", modifier = Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(0.dp, 0.dp, 0.dp, 12.dp)), contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFC3E2C2).copy(alpha = 0.4f), RoundedCornerShape(0.dp, 0.dp, 12.dp, 0.dp)), contentAlignment = Alignment.Center) {
                            Text("Aprendizaje", color = DarkTeal, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }
                    }
                }
            }

            // 4. SECCIÓN: MISIÓN Y VISIÓN
            item {
                Column(modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.background(Color(0xFFEAD7BB), RoundedCornerShape(12.dp)).padding(16.dp, 6.dp)) {
                        Text("✨ Nuestra Misión y Visión", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Misión", color = CoralPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black)
                            Text("Ofrecer juguetes que combinen diversión, aprendizaje y creatividad, brindando a cada niño experiencias que impulsen su desarrollo.", color = Color.DarkGray, fontSize = 11.sp, lineHeight = 15.sp)
                        }
                        Image(
                            painter = rememberAsyncImagePainter(model = "https://images.unsplash.com/photo-1502086223501-7ea6ecd79368?q=80&w=300"),
                            contentDescription = "Misión", modifier = Modifier.size(110.dp).clip(CircleShape).background(CoralPrimary).padding(4.dp).clip(CircleShape), contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("Visión", color = CoralPrimary, fontSize = 24.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.End)
                            Text("Ser la tienda de juguetes favorita de las familias, con productos seguros y divertidos que acompañen el crecimiento de los niños.", color = Color.DarkGray, fontSize = 11.sp, lineHeight = 15.sp, textAlign = TextAlign.End)
                        }
                    }
                }
            }

            // 5. BANNER DE INVITACIÓN AL CATÁLOGO
            item {
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFFFFC4C4).copy(alpha = 0.7f)).padding(24.dp)) {
                    Column(
                        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp)).padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("EXPLORA UN MUNDO DE JUGUETES PARA CADA EDAD", color = CoralPrimary, fontSize = 15.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Encuentra juguetes ideales para cada etapa, con opciones educativas, creativas y llenas de diversión para niños de 0 a 12 años.", color = Color.Gray, fontSize = 11.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = { navController.navigate("Tienda") }, colors = ButtonDefaults.buttonColors(containerColor = DeepNavy)) {
                            Text("Explorar catálogo", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // 6. FOOTER INSTITUCIONAL JUGUELAND
            item {
                Box(modifier = Modifier.fillMaxWidth().background(CoralPrimary).padding(16.dp, 24.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧸 JugueLand", color = DeepNavy, fontSize = 18.sp, fontWeight = FontWeight.Black)
                        Text("Tu juguetería de confianza. Todo para la diversión y aprendizaje en un solo lugar.", color = Color.White, fontSize = 10.sp, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("© 2026 ConsegueVentas. Todos los derechos reservados.", color = DeepNavy.copy(alpha = 0.6f), fontSize = 9.sp)
                    }
                }
            }
        }
    }
}