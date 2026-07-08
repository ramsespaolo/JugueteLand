package com.moviles.jugueteland.data.remote

import com.moviles.jugueteland.data.remote.dto.ApiResponse
import com.moviles.jugueteland.data.remote.dto.InsertarJugueteRequest
import com.moviles.jugueteland.data.remote.dto.ObtenerJuguetesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JuguetesApi {
    @POST("juguetes")
    suspend fun insertarJuguete(@Body request: InsertarJugueteRequest): Response<ApiResponse>
    // NUEVO: Pide la lista de juguetes a AWS
    @GET("juguetes")
    suspend fun obtenerJuguetes(): Response<ObtenerJuguetesResponse>
}