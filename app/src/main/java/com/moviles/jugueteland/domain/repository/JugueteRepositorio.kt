package com.moviles.jugueteland.domain.repository

import com.moviles.jugueteland.core.utils.Constantes
import com.moviles.jugueteland.data.remote.JuguetesApi
import com.moviles.jugueteland.data.remote.dto.InsertarJugueteRequest
import com.moviles.jugueteland.domain.model.juguete
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JugueteRepositorio {

    // 1. Configuramos Retrofit para conectarse a tu API Gateway de AWS
    private val api: JuguetesApi by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(JuguetesApi::class.java)
    }

    // 2. Tu lista estática original intacta (para que la Tienda siga funcionando)
    fun obtenerJuguetes(): List<juguete> {
        return listOf(
            juguete("1", "Funko Darth Vader", 69.90, 5f, "https://images.unsplash.com/photo-1608889174633-41a0c2346e75?q=80&w=300", false, true, "Colección"),
            juguete("2", "Bloques de Madera Eco", 49.90, 5f, "https://images.unsplash.com/photo-1515488042361-404e9250afef?q=80&w=300", true, false, "Educativos"),
            juguete("3", "Catan de Estrategia", 165.00, 5f, "https://images.unsplash.com/photo-1610890716171-6b1bb98ffd09?q=80&w=300", false, false, "Juegos de Mesa"),
            juguete("4", "Microscopio Mi Primer Lab", 149.90, 4f, "https://images.unsplash.com/photo-1532187863486-abf9d39d6618?q=80&w=300", true, false, "Educativos"),
            juguete("5", "Figura Marvel Legends", 119.90, 4f, "https://images.unsplash.com/photo-1608889175123-8ec330b86f84?q=80&w=300", false, false, "Colección"),
            juguete("6", "Peluche Jumbito Oso", 39.90, 5f, "https://images.unsplash.com/photo-1559251606-c623743a6d76?q=80&w=300", false, true, "Educativos")
        )
    }

    // 3. Nueva función asíncrona para enviar datos a AWS
    suspend fun agregarJugueteAWS(request: InsertarJugueteRequest): Boolean {
        return try {
            val response = api.insertarJuguete(request)
            // Validamos que el servidor devuelva código 200 y success = true
            response.isSuccessful && response.body()?.success == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    // 2. NUEVA FUNCIÓN: Descarga los juguetes desde DynamoDB
    suspend fun descargarCatalogoAWS(): List<juguete> {
        return try {
            val response = api.obtenerJuguetes()
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}