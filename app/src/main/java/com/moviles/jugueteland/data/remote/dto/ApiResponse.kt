package com.moviles.jugueteland.data.remote.dto

data class ApiResponse(
    val success: Boolean,
    val mensaje: String?,
    val error: String?
)