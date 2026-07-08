package com.moviles.jugueteland.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.moviles.jugueteland.domain.model.juguete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TiendaViewModel : ViewModel() {

    // 1. Inicializamos con una lista vacía. La fuente de la verdad ahora
    // es AWS, y TiendaScreen nos enviará los datos reales por aquí.
    private val _productos = MutableStateFlow<List<juguete>>(emptyList())
    val productos: StateFlow<List<juguete>> = _productos.asStateFlow()

    // 2. Almacena únicamente el texto que el usuario escribe.
    private val _textoBusqueda = MutableStateFlow("")
    val textoBusqueda: StateFlow<String> = _textoBusqueda.asStateFlow()

    // 3. Actualizamos el texto. ¡Ya no filtramos aquí adentro!
    // TiendaScreen se encargará del filtrado dinámico al detectar este cambio.
    fun actualizarBusqueda(texto: String) {
        _textoBusqueda.value = texto
    }

    // 4. Recibe y guarda el catálogo completo desde AWS
    fun actualizarProductos(nuevaLista: List<juguete>) {
        _productos.value = nuevaLista
    }
}