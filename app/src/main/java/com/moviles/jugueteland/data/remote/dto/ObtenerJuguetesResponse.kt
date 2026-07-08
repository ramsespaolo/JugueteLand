package com.moviles.jugueteland.data.remote.dto

import com.moviles.jugueteland.domain.model.juguete

data class ObtenerJuguetesResponse(
    val success: Boolean,
    val data: List<juguete>?
)