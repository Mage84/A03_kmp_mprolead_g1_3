package com.amonteiro.a03_kmp_mprolead_g1.di

import com.amonteiro.a03_kmp_mprolead_g1.data.remote.PhotographerAPI
import com.amonteiro.a03_kmp_mprolead_g1.presentation.viewmodel.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre au lancement de Koin
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(apiModule, viewModelModule)
    }
}

// Version pour iOS et Desktop
fun initKoin() = initKoin {}


val apiModule = module {
    single {
        HttpClient {
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
    }

//    single {
//       PhotographerAPI(get())
//    }

    singleOf(::PhotographerAPI)
}

val viewModelModule = module {

    //V1 : Si on veut ajouter manuellement certain paramètre
    //viewModel { MainViewModel(get(), Dispatchers.IO) }
    //V2 On déclare un Dispatchers à koin
    //factory { Dispatchers.IO }

    viewModelOf(::MainViewModel)
}