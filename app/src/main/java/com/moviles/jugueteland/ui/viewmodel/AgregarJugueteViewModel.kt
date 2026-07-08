package com.moviles.jugueteland.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviles.jugueteland.data.remote.dto.InsertarJugueteRequest
import com.moviles.jugueteland.domain.repository.JugueteRepositorio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

// Estado de la pantalla
data class AgregarUiState(
    val nombre: String = "",
    val precio: String = "",
    val categoria: String = "",
    val isLoading: Boolean = false,
    val mensaje: String? = null // Para mostrar éxito o error
)

class AgregarJugueteViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AgregarUiState())
    val uiState = _uiState.asStateFlow()

    private val repositorio = JugueteRepositorio()

    // Funciones para actualizar el texto mientras el usuario escribe
    fun onNombreChange(valor: String) { _uiState.value = _uiState.value.copy(nombre = valor) }
    fun onPrecioChange(valor: String) { _uiState.value = _uiState.value.copy(precio = valor) }
    fun onCategoriaChange(valor: String) { _uiState.value = _uiState.value.copy(categoria = valor) }
    fun limpiarMensaje() { _uiState.value = _uiState.value.copy(mensaje = null) }

    fun guardarJuguete() {
        val state = _uiState.value

        // Validación básica
        if (state.nombre.isBlank() || state.precio.isBlank() || state.categoria.isBlank()) {
            _uiState.value = _uiState.value.copy(mensaje = "Por favor, completa todos los campos")
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val request = InsertarJugueteRequest(
                id = UUID.randomUUID().toString(), // Genera un ID único para DynamoDB
                nombre = state.nombre,
                precio = state.precio.toDoubleOrNull() ?: 0.0,
                rating = 5f,
                imagenUrl = "https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?q=80&w=300", // Imagen por defecto
                tieneOferta = false,
                esNuevo = true,
                categoria = state.categoria
            )

            // ¡AQUÍ VIAJA A AWS!
            val exito = repositorio.agregarJugueteAWS(request)

            if (exito) {
                // Si AWS responde OK, limpiamos los campos y mostramos éxito
                _uiState.value = AgregarUiState(mensaje = "¡Juguete guardado en AWS DynamoDB!")
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, mensaje = "Error al guardar en la nube")
            }
        }
    }
}