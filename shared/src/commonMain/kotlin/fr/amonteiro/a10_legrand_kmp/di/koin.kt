package fr.amonteiro.a10_legrand_kmp.di

import fr.amonteiro.a10_legrand_kmp.data.fake.FakeWeatherAPI
import fr.amonteiro.a10_legrand_kmp.data.remote.WeatherApiDataSource
import fr.amonteiro.a10_legrand_kmp.domain.WeatherRepository
import fr.amonteiro.a10_legrand_kmp.presentation.MainViewModel
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
import org.koin.dsl.bind
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

//------------------------
//DECLARATION DES MODULES
//------------------------
val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }

    //V1 : On crée à la main les attentes de WeatherApiDataSource
    //single {
    //    val httpClient = HttpClient {...}
    //    WeatherApiDataSource(httpClient)
    //}
    //V2 : Get() injectera les objets déjà connus par koin, ici le HttpClient
    //single<WeatherRepository> { WeatherApiDataSource(get()) }


    //V3 : Si on a que des get()
    singleOf(::WeatherApiDataSource) bind WeatherRepository::class
}

val  fakeApiModule = module {
    singleOf(::FakeWeatherAPI) bind WeatherRepository::class
}

//Version spécifique au ViewModel
val viewModelModule = module {

    //V1 : Si on veut ajouter manuellement certain paramètre
    //viewModel { MainViewModel(get(), Dispatchers.IO) }
    //V2 On déclare un Dispatchers à koin
    //factory { Dispatchers.IO }

    viewModelOf(::MainViewModel)
}