package com.moviles.jugueteland.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import com.moviles.jugueteland.domain.model.juguete
import com.moviles.jugueteland.domain.repository.JugueteRepositorio
import com.moviles.jugueteland.ui.components.JugueToolbar
import com.moviles.jugueteland.ui.theme.*
import com.moviles.jugueteland.ui.viewmodel.CarritoGlobalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoDetalleScreen(
    navController: NavHostController,
    carritoViewModel: CarritoGlobalViewModel, // Motor inyectado
    productoId: String                        // ID dinámico inyectado desde DynamoDB
) {
    var cantidad by remember { mutableStateOf(1) }
    var colorSeleccionado by remember { mutableStateOf(0) } // 0: Azul, 1: Naranja

    // ESTADOS PARA CONTROLAR LA BÚSQUEDA DEL PRODUCTO EN AWS
    val repositorio = remember { JugueteRepositorio() }
    var producto by remember { mutableStateOf<juguete?>(null) }
    var estaCargando by remember { mutableStateOf(true) }

    // EFECTO DE INICIO: Buscamos el juguete real en el catálogo de AWS usando el ID
    LaunchedEffect(productoId) {
        val catalogoNube = repositorio.descargarCatalogoAWS()
        var productoEncontrado = catalogoNube.find { it.id == productoId }

        // Fallback: Si no hay internet o falla AWS, buscamos en la lista estática local
        if (productoEncontrado == null) {
            productoEncontrado = repositorio.obtenerJuguetes().find { it.id == productoId }
        }

        producto = productoEncontrado
        estaCargando = false
    }

    // Sincronización en tiempo real del Badge del Carrito
    val itemsCarrito by carritoViewModel.carrito.collectAsState()
    val cantidadTotal = itemsCarrito.sumOf { it.cantidad }

    // Lista estática para el carrusel de miniaturas visuales de Figma
    val miniaturas = listOf(
        producto?.imagenUrl ?: "https://images.unsplash.com/photo-1594787318286-3d835c1d207f?q=80&w=200",
        "https://images.unsplash.com/photo-1532187863486-abf9d39d6618?q=80&w=200",
        "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=200",
        "https://images.unsplash.com/photo-1559251606-c623743a6d76?q=80&w=200"
    )

    Scaffold(
        topBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                JugueToolbar(
                    titulo = "Detalle de producto",
                    navController = navController,
                    mostrarCarrito = true,
                    cantidadCarrito = cantidadTotal // Usa el número real y dinámico del badge
                )
            }
        },
        containerColor = BackgroundCream
    ) { paddingValues ->

        // Control de estado de carga mientras consulta a AWS
        if (estaCargando) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = CoralPrimary)
            }
            return@Scaffold
        }

        // Control de seguridad si el repositorio no encuentra el ID en ningún lado
        if (producto == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado", color = DeepNavy, fontWeight = FontWeight.Bold)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp, 16.dp)
        ) {
            // 1. IMAGEN PRINCIPAL (Dinámica)
            item {
                Image(
                    painter = rememberAsyncImagePainter(model = producto!!.imagenUrl),
                    contentDescription = producto!!.nombre,
                    modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)).background(Color.White),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // 2. CARRUSEL DE MINIATURAS VISUALES (Diseño Figma)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    miniaturas.forEach { url ->
                        Image(
                            painter = rememberAsyncImagePainter(model = url),
                            contentDescription = "Miniatura",
                            modifier = Modifier.size(65.dp).clip(RoundedCornerShape(8.dp)).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)).background(Color.White),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. CAJA DE DETALLES TÉCNICOS (Fusión Estilo + Datos Reales)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Nombre Real
                        Text(producto!!.nombre, color = DeepNavy, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))

                        // Rating Real Dinámico y Stock
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Row {
                                repeat(producto!!.rating.toInt()) {
                                    Icon(Icons.Default.Star, "", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                                }
                                repeat(5 - producto!!.rating.toInt()) {
                                    Icon(Icons.Default.Star, "", tint = Color.LightGray, modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("(16 reviews)", color = Color.Gray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("|  In Stock", color = DarkTeal, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        // Precio Real
                        Text("S/. ${String.format("%.2f", producto!!.precio)}", color = DeepNavy, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Descripción (O texto descriptivo por defecto)
                        Text("Modelo Premium de JugueteLand, fabricado con materiales altamente resistentes y seguros para el entretenimiento infantil.", color = Color.DarkGray, fontSize = 13.sp, lineHeight = 18.sp)

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Selección de Colores (Diseño Figma)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Colores:", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFF3A7BD5)).clickable { colorSeleccionado = 0 }.border(if (colorSeleccionado == 0) 2.dp else 0.dp, DeepNavy, CircleShape))
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color(0xFFE67E22)).clickable { colorSeleccionado = 1 }.border(if (colorSeleccionado == 1) 2.dp else 0.dp, DeepNavy, CircleShape))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Cantidad e Inyección Real al ViewModel
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.height(44.dp).border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { if (cantidad > 1) cantidad-- }) { Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                                Text(text = cantidad.toString(), modifier = Modifier.padding(horizontal = 12.dp), fontWeight = FontWeight.Bold)
                                IconButton(onClick = { cantidad++ }) { Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                            }

                            // BOTÓN AGREGAR (Manda los datos al carrito global y viaja)
                            Button(
                                onClick = {
                                    carritoViewModel.agregarAlCarrito(
                                        id = producto!!.id.toString(),
                                        nombre = producto!!.nombre,
                                        precio = producto!!.precio,
                                        cantidadNueva = cantidad,
                                        imagenUrl = producto!!.imagenUrl
                                    )
                                    navController.navigate("Carrito") {
                                        launchSingleTop = true
                                    }
                                },
                                modifier = Modifier.weight(1f).height(44.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Agregar carrito", fontWeight = FontWeight.Bold)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // INFO DE DESPACHO (Diseño Figma)
                        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = BackgroundCream.copy(alpha = 0.4f)), shape = RoundedCornerShape(8.dp)) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.ShoppingCart, "", tint = DeepNavy, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Envío gratuito", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("Ingresa tu código postal para verificar disponibilidad.", color = Color.Gray, fontSize = 11.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Refresh, "", tint = DeepNavy, modifier = Modifier.size(20.dp))
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text("Devoluciones", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text("Devolución gratuita dentro de los 30 días posteriores a la entrega.", color = Color.Gray, fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // 4. SECCIÓN: OFERTAS PARA SORPRENDERLOS (Diseño Figma)
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.background(Color(0xFFC3E2C2), RoundedCornerShape(12.dp)).padding(16.dp, 6.dp)) {
                        Text("🎁 Ofertas para sorprenderlos", color = DeepNavy, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text("Descuentos especiales por tiempo limitado", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Feed sugerido inferior
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) { ProductoMiniCard("Peluche Osito Gigante", "S/. 45.90", "https://images.unsplash.com/photo-1559251606-c623743a6d76?q=80&w=200") }
                    Box(modifier = Modifier.weight(1f)) { ProductoMiniCard("Bloques de madera", "S/. 45.90", "https://images.unsplash.com/photo-1515488042361-404e9250afef?q=80&w=200") }
                }
            }
        }
    }
}

@Composable
fun ProductoMiniCard(titulo: String, precio: String, imagenUrl: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(12.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(painter = rememberAsyncImagePainter(model = imagenUrl), contentDescription = "", modifier = Modifier.fillMaxWidth().height(100.dp).clip(RoundedCornerShape(8.dp)), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.height(6.dp))
            Text(titulo, color = DeepNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(precio, color = CoralPrimary, fontSize = 13.sp, fontWeight = FontWeight.Black)
        }
    }
}