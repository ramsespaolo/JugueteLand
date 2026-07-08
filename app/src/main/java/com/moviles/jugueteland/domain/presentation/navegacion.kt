package com.moviles.jugueteland.domain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.moviles.jugueteland.screens.*
import com.moviles.jugueteland.ui.theme.CoralPrimary
import com.moviles.jugueteland.ui.viewmodel.CarritoGlobalViewModel
import kotlinx.coroutines.launch

sealed class Pantalla(val ruta: String) {
    object Inicio : Pantalla("inicio")
    object Tienda : Pantalla("tienda")
    object Novedades : Pantalla("novedades")
}

@Composable
fun GrafoNavegacionJugueteLand(navController: NavHostController) {
    val estadoMenu = rememberDrawerState(initialValue = DrawerValue.Closed)
    val ambitoCorrutina = rememberCoroutineScope()
    val carritoViewModel: CarritoGlobalViewModel = viewModel()
    val opcionesMenu = listOf("Inicio", "Tienda", "Novedades", "Sobre nosotros", "Contacto","Agregar Juguete")

    ModalNavigationDrawer(
        drawerState = estadoMenu,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize().background(CoralPrimary)) {
                    Spacer(modifier = Modifier.height(24.dp))
                    opcionesMenu.forEach { opcion ->
                        NavigationDrawerItem(
                            label = { Text(opcion, color = Color.White, fontSize = 16.sp) },
                            selected = false,
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent,
                                selectedContainerColor = Color.Transparent
                            ),
                            onClick = {
                                ambitoCorrutina.launch { estadoMenu.close() }
                                when (opcion) {
                                    "Inicio" -> navController.navigate(Pantalla.Inicio.ruta) { launchSingleTop = true }
                                    "Tienda" -> navController.navigate(Pantalla.Tienda.ruta) { launchSingleTop = true }
                                    "Novedades" -> navController.navigate(Pantalla.Novedades.ruta) { launchSingleTop = true }
                                    "Sobre nosotros" -> navController.navigate("SobreNosotros") { launchSingleTop = true }
                                    "Contacto" -> navController.navigate("Contacto") { launchSingleTop = true }
                                    "Agregar Juguete" -> navController.navigate("AgregarJuguete") { launchSingleTop = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Pantalla.Inicio.ruta) {
            composable(Pantalla.Inicio.ruta) {
                HomeScreen(navController = navController, drawerState = estadoMenu, scope = ambitoCorrutina)
            }
            composable(Pantalla.Tienda.ruta) {
                // Le pasamos el carritoViewModel a la tienda para que sepa cuántos hay
                TiendaScreen(navController = navController, drawerState = estadoMenu, scope = ambitoCorrutina, carritoViewModel = carritoViewModel)
            }
            composable(Pantalla.Novedades.ruta) { NovedadesScreen(navController = navController) }
            composable("SobreNosotros") { SobreNosotrosScreen(navController = navController) }
            composable("Contacto") { ContactoScreen(navController = navController) }

            // NUEVO: La ruta ahora espera un ID (ejemplo: ProductoDetalle/2)
            composable(
                route = "ProductoDetalle/{productoId}",
                arguments = listOf(navArgument("productoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getString("productoId") ?: "1"
                ProductoDetalleScreen(navController = navController, carritoViewModel = carritoViewModel, productoId = productoId)
            }

            composable("Carrito") { CarritoScreen(navController = navController, carritoViewModel = carritoViewModel) }
            composable("Checkout") { CheckoutScreen(navController, carritoViewModel = carritoViewModel) }
            composable("Seguimiento") { SeguimientoScreen(navController = navController) }
            composable("AgregarJuguete") { AgregarJugueteScreen(navController = navController) }
        }
    }
}