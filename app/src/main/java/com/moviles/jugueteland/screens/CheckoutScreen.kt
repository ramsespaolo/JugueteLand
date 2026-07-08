package com.moviles.jugueteland.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.moviles.jugueteland.domain.presentation.Pantalla
import com.moviles.jugueteland.ui.components.JugueToolbar
import com.moviles.jugueteland.ui.viewmodel.CarritoGlobalViewModel

// Colores basados en tus imágenes de referencia
val BackgroundCream = Color(0xFFFDF5E6)
val DeepNavy = Color(0xFF1A3644)
val SoftOrange = Color(0xFFF2A384)
val ButtonSlate = Color(0xFF4F6D7A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    carritoViewModel: CarritoGlobalViewModel // Recibimos el ViewModel
) {
    // Estados del formulario de facturación
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("Lima") }
    var datosAdicionales by remember { mutableStateOf("") }

    // --- ESTADOS DE ERROR (VALIDACIÓN) ---
    var errorNombre by remember { mutableStateOf(false) }
    var errorCorreo by remember { mutableStateOf(false) }
    var errorTelefono by remember { mutableStateOf(false) }
    var errorDireccion by remember { mutableStateOf(false) }
    var errorDni by remember { mutableStateOf(false) }

    // Estado del Radio Button de pago (false = Tarjeta, true = Contra entrega)
    var pagoContraEntrega by remember { mutableStateOf(true) }

    // Estado para controlar el diálogo emergente de confirmación
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    // Obtenemos los datos dinámicos del carrito
    val listaCarrito by carritoViewModel.carrito.collectAsState()
    val subtotal by carritoViewModel.subtotal.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                // Barra de aviso superior fija
                Box(
                    modifier = Modifier.fillMaxWidth().background(SoftOrange).padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚚 Envío gratis desde S/.199 a todo el Perú", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                JugueToolbar(
                    titulo = "Dirección de facturación",
                    navController = navController,
                    onMenuClick = null,
                    mostrarCarrito = false
                )
            }
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- BLOQUE 1: DIRECCIÓN DE FACTURACIÓN (Formulario con validación) ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Dirección de facturación", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp)

                        CheckoutTextField(label = "Nombre completo *", value = nombre, onValueChange = { nombre = it; errorNombre = false }, placeholder = "Juan Pérez García", isError = errorNombre, supportingText = if(errorNombre) "Campo obligatorio" else null)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) { CheckoutTextField(label = "Correo electrónico *", value = correo, onValueChange = { correo = it; errorCorreo = false }, placeholder = "ejemplo@correo.com", isError = errorCorreo, supportingText = if(errorCorreo) "Correo inválido" else null) }
                            Box(modifier = Modifier.weight(1f)) { CheckoutTextField(label = "Teléfono de contacto *", value = telefono, onValueChange = { telefono = it; errorTelefono = false }, placeholder = "+51 999 999 999", isError = errorTelefono, supportingText = if(errorTelefono) "Mínimo 9 dígitos" else null) }
                        }

                        CheckoutTextField(label = "Dirección completa *", value = direccion, onValueChange = { direccion = it; errorDireccion = false }, placeholder = "Av. Principal 123, Distrito", isError = errorDireccion, supportingText = if(errorDireccion) "Campo obligatorio" else null)
                        CheckoutTextField(label = "DNI *", value = dni, onValueChange = { if(it.length <= 8) { dni = it; errorDni = false } }, placeholder = "12345678", isError = errorDni, supportingText = if(errorDni) "Debe tener 8 dígitos" else null)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.weight(1f)) { CheckoutTextField(label = "Ciudad *", value = ciudad, onValueChange = { ciudad = it }, placeholder = "Lima") }
                            Box(modifier = Modifier.weight(1f)) { CheckoutTextField(label = "Datos Adicionales *", value = datosAdicionales, onValueChange = { datosAdicionales = it }, placeholder = "Int, Dpt, Piso") }
                        }
                    }
                }
            }

            // --- BLOQUE 2: RESUMEN DEL PEDIDO DINÁMICO ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Resumen del Pedido", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        HorizontalDivider()

                        listaCarrito.forEach { item ->
                            ResumenProductoRow(
                                nombre = item.nombre,
                                desc = "Juguete / Accesorio",
                                cant = "x${item.cantidad}",
                                precio = "S/. ${String.format("%.2f", item.precio * item.cantidad)}"
                            )
                        }

                        HorizontalDivider()
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal", color = Color.Gray)
                            Text("S/. ${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Medium)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Envío", color = Color.Gray)
                            Text("Gratis", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider()
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                            Text("S/. ${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 16.sp)
                        }
                    }
                }
            }

            // --- BLOQUE 3: MÉTODO DE PAGO ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Método de Pago", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                selected = !pagoContraEntrega,
                                onClick = { pagoContraEntrega = false },
                                colors = RadioButtonDefaults.colors(selectedColor = SoftOrange)
                            )
                            Column { Text("Pago con Tarjeta", fontWeight = FontWeight.Medium, fontSize = 13.sp) }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            RadioButton(
                                selected = pagoContraEntrega,
                                onClick = { pagoContraEntrega = true },
                                colors = RadioButtonDefaults.colors(selectedColor = SoftOrange)
                            )
                            Column {
                                Text("Pago contra entrega", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                                Text("Pago en efectivo al recibir tu pedido", color = Color.Gray, fontSize = 11.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                // --- VALIDACIÓN FINAL ---
                                errorNombre = nombre.isBlank()
                                errorCorreo = !correo.contains("@")
                                errorTelefono = telefono.length < 9
                                errorDireccion = direccion.isBlank()
                                errorDni = dni.length != 8

                                if (!errorNombre && !errorCorreo && !errorTelefono && !errorDireccion && !errorDni) {
                                    mostrarConfirmacion = true
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = ButtonSlate),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Finalizar compra", color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        TextButton(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
                            Text("← Volver al carrito", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }

    // --- POP-UP DIÁLOGO DE CONFIRMACIÓN ---
    if (mostrarConfirmacion) {
        Dialog(onDismissRequest = { mostrarConfirmacion = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier.size(72.dp).background(Color(0xFFC47B2B), shape = RoundedCornerShape(36.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Éxito", tint = Color.White, modifier = Modifier.size(40.dp))
                    }

                    Text("Pago Enviado !!!!!", color = DeepNavy, fontWeight = FontWeight.Black, fontSize = 24.sp, textAlign = TextAlign.Center)

                    Text(
                        text = "Pago completado, se le mandará en el E-mail su Comprobante de pago, la página de seguimiento de guía",
                        color = Color.Gray, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* Acción para descargar */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonSlate),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Descargar comprobante", color = Color.White, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            mostrarConfirmacion = false
                            carritoViewModel.vaciarCarrito() // Limpiamos el carrito
                            navController.navigate("Seguimiento") {
                                popUpTo(Pantalla.Inicio.ruta) { inclusive = false }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonSlate),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Seguimiento de pedido", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// COMPONENTE ACTUALIZADO PARA ACEPTAR VALIDACIONES
@Composable
fun CheckoutTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean = false,
    supportingText: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = DeepNavy)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 12.sp, color = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) Color.Red else SoftOrange,
                unfocusedBorderColor = Color.LightGray
            ),
            singleLine = true
        )
        if (isError && supportingText != null) {
            Text(supportingText, color = Color.Red, fontSize = 10.sp)
        }
    }
}

@Composable
fun ResumenProductoRow(nombre: String, desc: String, cant: String, precio: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text(nombre, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
            Text(desc, fontSize = 11.sp, color = Color.Gray)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(cant, fontSize = 12.sp, color = Color.Gray)
            if (precio.isNotEmpty()) {
                Text(precio, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}