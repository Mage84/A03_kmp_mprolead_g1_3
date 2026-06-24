package com.amonteiro.a03_kmp_mprolead_g1.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable //KotlinX impose cette annotation
data class MuseumDTO(
    val objectID: Int,
    val title: String,
    val artistDisplayName: String,
    val primaryImage: String,
)

suspend fun main() {
    println(KtorMuseumApi.loadMuseums().joinToString(separator = "\n\n"))

    //Pour que le programme s'arrête, inutile sur Android
    KtorMuseumApi.close()
}

object KtorMuseumApi {
    private const val API_URL =
        "https://raw.githubusercontent.com/Kotlin/KMP-App-Template/main/list.json"

    //Déclaration du client
    private val client  = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        //Proxy
        //engine {
        //    proxy = ProxyBuilder.http("monproxy:1234")
        //}
    }

    //GET
    suspend fun loadMuseums(): List<MuseumDTO> {
        val response = client.get(API_URL){
//            headers {
//                append("Authorization", "Bearer YOUR_TOKEN")
//                append("Custom-Header", "CustomValue")
//            }
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body()
    }

    //POST
    suspend fun postData(newObject: MuseumDTO): MuseumDTO {
        val response = client.post(API_URL){
            setBody(newObject)
        }
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }
        return response.body()
    }

    //Avec Flow
    fun loadMuseumsFlow() = flow<List<MuseumDTO>> {
        emit(client.get(API_URL).body())
    }

    fun close() {
        client.close()
    }
}