package com.moviles.jugueteland.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
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
import com.moviles.jugueteland.domain.presentation.Pantalla
import com.moviles.jugueteland.ui.theme.*
import com.moviles.jugueteland.ui.viewmodel.CarritoGlobalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavHostController,
    carritoViewModel: CarritoGlobalViewModel
) {
    val listaCarrito by carritoViewModel.carrito.collectAsState()
    val subtotal by carritoViewModel.subtotal.collectAsState()
    val total = subtotal

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito de Compras", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoralPrimary)
            )
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        if (listaCarrito.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Tu carrito está vacío", color = DeepNavy, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate(Pantalla.Tienda.ruta) },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkTeal)
                    ) {
                        Text("Ir a la Tienda")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp, 16.dp)
            ) {
                items(listaCarrito, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = item.imagenUrl),
                                    contentDescription = "",
                                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.nombre, color = DeepNavy, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 2)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("S/. ${item.precio}", color = DeepNavy, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                    Spacer(modifier = Modifier.height(6.dp))

                                    Row(
                                        modifier = Modifier.height(32.dp).border(1.dp, Color.LightGray, RoundedCornerShape(6.dp)),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { carritoViewModel.cambiarCantidad(item.id, false) }) { Text("-") }
                                        Text(item.cantidad.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        IconButton(onClick = { carritoViewModel.cambiarCantidad(item.id, true) }) { Text("+") }
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("S/. ${String.format("%.2f", item.precio * item.cantidad)}", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Black)
                            }

                            Box(
                                modifier = Modifier.align(Alignment.TopStart).padding(top = 4.dp, start = 4.dp).size(20.dp).clip(CircleShape).background(Color(0xFFE67E22)).padding(2.dp)
                            ) {
                                IconButton(
                                    onClick = { carritoViewModel.eliminarDelCarrito(item.id) },
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "", tint = Color.White)
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Resumen de Pedido", color = DeepNavy, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp).height(IntrinsicSize.Min)) {
                            Column(modifier = Modifier.weight(1.2f)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Subtotal", color = Color.Gray, fontSize = 13.sp)
                                    Text("S/. ${String.format("%.2f", subtotal)}", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Envio", color = Color.Gray, fontSize = 13.sp)
                                    Text("S/. 0.00", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Text("Envio gratuito", color = Color(0xFFE67E22), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }

                            VerticalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)

                            Column(modifier = Modifier.weight(1.5f)) {
                                Text("Dirección", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Text("Enviar a Av. Los Próceres 742, San Juan de Lurigancho, Lima", color = DeepNavy, fontSize = 11.sp, lineHeight = 14.sp)
                                Text("Cambiar dirección", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text("Total", color = DeepNavy, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text("S/. ${String.format("%.2f", total)}", color = DeepNavy, fontSize = 16.sp, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Button(
                        onClick = { navController.navigate("Checkout") { launchSingleTop = true } },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkTeal),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Proceder al Pago", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { navController.navigate(Pantalla.Tienda.ruta) { launchSingleTop = true } },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy),
                        border = BorderStroke(1.5.dp, DeepNavy)
                    ) {
                        Text("Seguir Comprando", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}