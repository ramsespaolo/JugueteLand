package com.moviles.jugueteland.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.moviles.jugueteland.ui.theme.CoralPrimary
import com.moviles.jugueteland.ui.theme.DeepNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugueToolbar(
    titulo: String,
    navController: NavHostController,
    onMenuClick: (() -> Unit)? = null,
    mostrarCarrito: Boolean = true,
    cantidadCarrito: Int = 0 // NUEVO: Recibe la cantidad de juguetes
) {
    TopAppBar(
        title = { Text(titulo, color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        navigationIcon = {
            if (onMenuClick != null) {
                IconButton(onClick = onMenuClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú", tint = DeepNavy)
                }
            } else {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás", tint = DeepNavy)
                }
            }
        },
        actions = {
            if (mostrarCarrito) {
                IconButton(onClick = { navController.navigate("Carrito") }) {
                    // AQUÍ ESTÁ LA MAGIA: El globo rojo con el número
                    if (cantidadCarrito > 0) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = Color.Red, contentColor = Color.White) {
                                    Text(cantidadCarrito.toString())
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = DeepNavy)
                        }
                    } else {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito", tint = DeepNavy)
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = CoralPrimary)
    )
}