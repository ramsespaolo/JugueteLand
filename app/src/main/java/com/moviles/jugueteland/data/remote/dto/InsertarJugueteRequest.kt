package com.moviles.jugueteland.data.remote.dto

data class InsertarJugueteRequest(
    val id: String,
    val nombre: String,
    val precio: Double,
    val rating: Float,
    val imagenUrl: String,
    val tieneOferta: Boolean,
    val esNuevo: Boolean,
    val categoria: String
)