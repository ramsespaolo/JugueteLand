package com.moviles.jugueteland.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.moviles.jugueteland.domain.repository.JugueteRepositorio
import com.moviles.jugueteland.ui.components.JugueToolbar
import com.moviles.jugueteland.ui.theme.*
import com.moviles.jugueteland.ui.viewmodel.CarritoGlobalViewModel
import com.moviles.jugueteland.ui.viewmodel.TiendaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaScreen(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    tiendaViewModel: TiendaViewModel = viewModel(),
    carritoViewModel: CarritoGlobalViewModel // Inyección del estado real del carrito
) {
    // OBSERVADORES DE ARQUITECTURA REAL (Primer bloque de código)
    val productosReales by tiendaViewModel.productos.collectAsState()
    val textBusqueda by tiendaViewModel.textoBusqueda.collectAsState()

    // Sincronización del conteo real de la burbuja flotante del carrito
    val itemsCarrito by carritoViewModel.carrito.collectAsState()
    val cantidadTotal = itemsCarrito.sumOf { it.cantidad }

    // ESTADO LOCAL PARA LOS CHIPS (Diseño del segundo bloque de código)
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    val categoriasFiltro = listOf("Todos", "Educativos", "Colección", "Juegos de Mesa")

    // ESTADOS PARA CONTROLAR LA DESCARGA DESDE AWS
    val repositorio = remember { JugueteRepositorio() }
    var estaCargando by remember { mutableStateOf(true) }

    // EFECTO DE INICIO: Descarga el catálogo real de AWS al abrir la pantalla
    LaunchedEffect(Unit) {
        val juguetesDeAWS = repositorio.descargarCatalogoAWS()
        val juguetesLocales = repositorio.obtenerJuguetes()

        // Sumamos ambas listas para no perder los datos de prueba
        val catalogoCompleto = juguetesLocales + juguetesDeAWS

        tiendaViewModel.actualizarProductos(catalogoCompleto)
        estaCargando = false
    }

    // FILTRADO HÍBRIDO EN TIEMPO REAL: Combina la búsqueda del ViewModel con los Chips de diseño
    val productosFiltrados = productosReales.filter { producto ->
        // Adaptamos la validación del pilar/categoría según la estructura del modelo real
        val cumpleFiltro = categoriaSeleccionada == "Todos" ||
                (producto.categoria.equals(categoriaSeleccionada, ignoreCase = true))

        // Aplicamos también el filtro de texto desde la barra de búsqueda
        val cumpleBusqueda = producto.nombre.contains(textBusqueda, ignoreCase = true)

        cumpleFiltro && cumpleBusqueda
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)) {
                // Barra Superior Unificada con Badge Dinámico
                JugueToolbar(
                    titulo = "Catálogo JugueLand",
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    mostrarCarrito = true,
                    cantidadCarrito = cantidadTotal
                )

                // Barra de Búsqueda Integrada Vinculada al ViewModel
                Box(modifier = Modifier.fillMaxWidth().background(CoralPrimary).padding(16.dp, 0.dp, 16.dp, 12.dp)) {
                    OutlinedTextField(
                        value = textBusqueda,
                        onValueChange = { tiendaViewModel.actualizarBusqueda(it) }, // Lógica del ViewModel
                        placeholder = { Text("Buscar juguetes, funkos, juegos...", fontSize = 14.sp) },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Buscar", tint = Color.Gray) },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White, unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                        ),
                        singleLine = true
                    )
                }
            }
        },
        containerColor = BackgroundCream
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {

            // SECCIÓN HORIZONTAL: CHIPS DE FILTRO (Fiel al diseño visual de Figma)
            LazyRow(
                contentPadding = PaddingValues(16.dp, 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categoriasFiltro) { categoria ->
                    val esSeleccionado = categoriaSeleccionada == categoria
                    FilterChip(
                        selected = esSeleccionado,
                        onClick = { categoriaSeleccionada = categoria },
                        label = {
                            Text(
                                text = categoria,
                                color = if (esSeleccionado) Color.White else DeepNavy,
                                fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DarkTeal,
                            containerColor = Color(0xB3FFFFFF),
                            labelColor = DeepNavy,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = esSeleccionado,
                            borderColor = Color.LightGray,
                            selectedBorderColor = DarkTeal
                        )
                    )
                }
            }

            // Texto informativo que cambia de forma dinámica según las acciones
            Text(
                text = "Mostrando ${productosFiltrados.size} productos en $categoriaSeleccionada",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp, 0.dp, 16.dp, 8.dp)
            )

            // MANEJO DE ESTADO DE CARGA Y RENDERIZADO DE LA GRILLA
            if (estaCargando) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = CoralPrimary)
                }
            } else if (productosFiltrados.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No se encontraron juguetes.", color = Color.Gray)
                }
            } else {
                // GRILLA DINÁMICA DE DOS COLUMNAS REAL: Renderiza con navegación por ID seguro
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp, 0.dp, 12.dp, 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize().weight(1f) // Se asegura de no desbordar
                ) {
                    items(productosFiltrados) { producto ->
                        com.moviles.jugueteland.ui.components.JugueteCard(
                            nombre = producto.nombre,
                            precio = producto.precio,
                            rating = producto.rating,
                            imagenUrl = producto.imagenUrl,
                            etiqueta = if (producto.tieneOferta) "Oferta" else if (producto.esNuevo) "Nuevo" else "",
                            // MAGIA COMBINADA: Navegamos al detalle enviando el ID real e inyectado
                            onComprarClick = { navController.navigate("ProductoDetalle/${producto.id}") }
                        )
                    }
                }
            }
        }
    }
}