package com.moviles.jugueteland.domain.model

data class ItemCarrito(
    val id: String,
    val nombre: String,
    val precio: Double,
    var cantidad: Int,
    val imagenUrl: String
)