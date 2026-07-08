package com.moviles.jugueteland.domain.model

data class juguete(
    val id: String,
    val nombre: String,
    val precio: Double,
    val rating: Float,
    val imagenUrl: String,
    val esNuevo: Boolean = false,
    val tieneOferta: Boolean = false,
    val categoria: String
)
