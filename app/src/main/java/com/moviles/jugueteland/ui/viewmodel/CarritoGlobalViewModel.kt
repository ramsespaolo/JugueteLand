package com.moviles.jugueteland.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.moviles.jugueteland.domain.model.ItemCarrito
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CarritoGlobalViewModel : ViewModel() {

    private val _carrito = MutableStateFlow<List<ItemCarrito>>(emptyList())
    val carrito: StateFlow<List<ItemCarrito>> = _carrito.asStateFlow()

    private val _subtotal = MutableStateFlow(0.0)
    val subtotal: StateFlow<Double> = _subtotal.asStateFlow()

    fun agregarAlCarrito(id: String, nombre: String, precio: Double, cantidadNueva: Int, imagenUrl: String) {
        _carrito.update { listaActual ->
            val itemExistente = listaActual.find { it.id == id }
            if (itemExistente != null) {
                listaActual.map {
                    if (it.id == id) it.copy(cantidad = it.cantidad + cantidadNueva) else it
                }
            } else {
                listaActual + ItemCarrito(id, nombre, precio, cantidadNueva, imagenUrl)
            }
        }
        calcularTotal()
    }

    fun cambiarCantidad(id: String, sumar: Boolean) {
        _carrito.update { lista ->
            lista.map {
                if (it.id == id) {
                    val nuevaCantidad = if (sumar) it.cantidad + 1 else it.cantidad - 1
                    it.copy(cantidad = if (nuevaCantidad > 0) nuevaCantidad else 1)
                } else it
            }
        }
        calcularTotal()
    }

    fun eliminarDelCarrito(id: String) {
        _carrito.update { lista -> lista.filter { it.id != id } }
        calcularTotal()
    }

    // Función reubicada correctamente dentro de la clase
    fun vaciarCarrito() {
        _carrito.value = emptyList<com.moviles.jugueteland.domain.model.ItemCarrito>()
        calcularTotal()
    }

    private fun calcularTotal() {
        _subtotal.value = _carrito.value.sumOf { it.precio * it.cantidad }
    }
}