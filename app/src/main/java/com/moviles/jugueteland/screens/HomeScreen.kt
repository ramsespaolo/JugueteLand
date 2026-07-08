package com.moviles.jugueteland.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.moviles.jugueteland.ui.theme.*
import coil.compose.rememberAsyncImagePainter
import com.moviles.jugueteland.domain.presentation.Pantalla
import com.moviles.jugueteland.ui.components.JugueToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SoftOrange)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🚚 Envío gratis desde S/.199 a todo el Perú",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                JugueToolbar(
                    titulo = "JugueteLand",
                    navController = navController,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    mostrarCarrito = true
                )
            }
        },
        containerColor = BackgroundCream
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            item {
                // CORRECCIÓN: Usamos Pantalla.Tienda.ruta
                HeroBannerSection(onComprarClick = { navController.navigate(Pantalla.Tienda.ruta) })
            }

            item { CuponOfertaSection() }

            item {
                // CORRECCIÓN: Usamos Pantalla.Tienda.ruta
                CategoriasSection(onCategoriaClick = { navController.navigate(Pantalla.Tienda.ruta) })
            }

            item {
                // CORRECCIÓN: Usamos Pantalla.Tienda.ruta
                ProductosDestacadosSection(onProductoClick = { navController.navigate(Pantalla.Tienda.ruta) })
            }

            item {
                // CORRECCIÓN: Usamos Pantalla.Tienda.ruta
                OfertasSemanaSection(onProductoClick = { navController.navigate(Pantalla.Tienda.ruta) })
            }

            item {
                MarcasAliadasSection()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==================== COMPONENTES DE INTERFAZ ====================

@Composable
fun HeroBannerSection(onComprarClick: () -> Unit) {
    val banners = listOf(
        Pair("🚀 PIEZAS DE COLECCIÓN", "https://images.unsplash.com/photo-1608889174633-41a0c2346e75?q=80&w=600"),
        Pair("🧠 JUGUETES EDUCATIVOS\nY APRENDIZAJE STEM", "https://images.unsplash.com/photo-1515488042361-404e9250afef?q=80&w=600"),
        Pair("🎲 NOCHES DE JUEGOS\nDE MESA EN FAMILIA", "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=600")
    )

    val pagerState = rememberPagerState(pageCount = { banners.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        HorizontalPager (state = pagerState) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberAsyncImagePainter(model = banners[page].second),
                    contentDescription = "Fondo Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = banners[page].first,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onComprarClick,
                        colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Ver catálogo", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(banners.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }
    }
}

@Composable
fun CuponOfertaSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftOrange.copy(alpha = 0.12f)),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, SoftOrange.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "🎟️ ¡DESCUENTO DE BIENVENIDA!", color = DeepNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(text = "Usa el código: JUGUETE10 y gana 10% OFF", color = Color.DarkGray, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .background(SoftOrange, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Copiar", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategoriasSection(onCategoriaClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "🧸 Buscar por Edad", color = DeepNavy, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(text = "Los preferidos para el público infantil", color = Color.Gray, fontSize = 11.sp)

        Spacer(modifier = Modifier.height(14.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            CategoriaItem("Bebés", "0-3", Color(0xFFC3E2C2), onCategoriaClick)
            CategoriaItem("Niños", "3-6", Color(0xFFFFC4C4), onCategoriaClick)
            CategoriaItem("Preescolares", "6-9", Color(0xFFEAD7BB), onCategoriaClick)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            CategoriaItem("Escolares", "9-12", Color(0xFFFFE5CA), onCategoriaClick)
            Spacer(modifier = Modifier.width(24.dp))
            CategoriaItem("Pre-Adolescentes", "12+", Color(0xFFD4E2D4), onCategoriaClick)
        }
    }
}

@Composable
fun CategoriaItem(nombre: String, edad: String, colorFondo: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() })
    {
        Box(modifier = Modifier
            .size(64.dp)
            .background(colorFondo, CircleShape),
            contentAlignment = Alignment.Center)
        {
            Text(text = edad, color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = nombre, color = DeepNavy, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ProductosDestacadosSection(onProductoClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "⭐ Productos Destacados", color = DeepNavy, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "Lo más top de nuestro ecosistema vertical", color = Color.Gray, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Box(modifier = Modifier.width(150.dp)) {
                    com.moviles.jugueteland.ui.components.JugueteCard(
                        nombre = "Funko Darth Vader", precio = 69.90, rating = 5f,
                        imagenUrl = "https://images.unsplash.com/photo-1608889174633-41a0c2346e75?q=80&w=300",
                        etiqueta = "Colección", onComprarClick = onProductoClick
                    )
                }
            }
            item {
                Box(modifier = Modifier.width(150.dp)) {
                    com.moviles.jugueteland.ui.components.JugueteCard(
                        nombre = "Bloques de Madera Eco", precio = 49.90, rating = 5f,
                        imagenUrl = "https://images.unsplash.com/photo-1515488042361-404e9250afef?q=80&w=300",
                        etiqueta = "Educativo", onComprarClick = onProductoClick
                    )
                }
            }
            item {
                Box(modifier = Modifier.width(150.dp)) {
                    com.moviles.jugueteland.ui.components.JugueteCard(
                        nombre = "Catan de Estrategia", precio = 165.00, rating = 5f,
                        imagenUrl = "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=300",
                        etiqueta = "Juego Mesa", onComprarClick = onProductoClick
                    )
                }
            }
        }
    }
}

@Composable
fun OfertasSemanaSection(onProductoClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "✨ Ofertas de la Semana", color = DeepNavy, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(text = "Precios especiales por tiempo limitado", color = Color.Gray, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            com.moviles.jugueteland.ui.components.JugueteCard(
                nombre = "Peluche Jumbito", precio = 39.90, rating = 5f,
                imagenUrl = "https://images.unsplash.com/photo-1559251606-c623743a6d76?q=80&w=300",
                etiqueta = "-20%", onComprarClick = onProductoClick, modifier = Modifier.weight(1f)
            )
            com.moviles.jugueteland.ui.components.JugueteCard(
                nombre = "Camioneta Rally 4x4", precio = 59.90, rating = 4f,
                imagenUrl = "https://images.unsplash.com/photo-1594787318286-3d835c1d207f?q=80&w=300",
                etiqueta = "-15%", onComprarClick = onProductoClick, modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MarcasAliadasSection() {
    val marcasLogos = listOf(
        "https://images.unsplash.com/photo-1599305445671-ac2c0869a43a?q=80&w=150",
        "https://images.unsplash.com/photo-1516874592-3c8612fe4370?q=80&w=150"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Nuestras Marcas Aliadas",
            color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            items(marcasLogos) { url ->
                Card(
                    modifier = Modifier.size(width = 90.dp, height = 45.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Image(
                            painter = rememberAsyncImagePainter(model = url),
                            contentDescription = "Marca",
                            modifier = Modifier.fillMaxSize().padding(6.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}