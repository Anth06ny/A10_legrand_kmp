package fr.amonteiro.a10_legrand_kmp.data.remote

import fr.amonteiro.a10_legrand_kmp.di.initKoin
import fr.amonteiro.a10_legrand_kmp.domain.WeatherRepository
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable
import org.koin.mp.KoinPlatform

suspend fun main() {

    initKoin()
    val weatherApiDataSource: WeatherApiDataSource =
        KoinPlatform.getKoin().get<WeatherApiDataSource>()

    weatherApiDataSource.loadWeathers("Nice").forEach {
        println(it.getResume())
    }
}

class WeatherApiDataSource(val client: HttpClient) : WeatherRepository {

    fun loadWeathersWithFlow(cityName: String) = flow<List<Weather>> {
        emit(loadWeathers(cityName))
    }


    override suspend fun loadWeathers(cityName: String): List<Weather> {

        val response =
            client.get("https://api.openweathermap.org/data/2.5/find?q=$cityName&appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr")

        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body<WeatherResultDTO>().list.map {
            val icon = it.weather.firstOrNull()?.icon ?: ""
            Weather(
                id = it.id,
                name = it.name,
                temp = it.main.temp,
                speed = it.wind.speed,
                description = it.weather.firstOrNull()?.description ?: "-",
                icon = "https://openweathermap.org/img/wn/${icon}@4x.png",
            )
        }
    }

    fun close() {
        client.close()
    }
}

/* -------------------------------- */
// WEATHER
/* -------------------------------- */
@Serializable
data class WeatherResultDTO(val list: List<WeatherDTO>)

@Serializable
data class WeatherDTO(
    val id: Int, val name: String, var main: TempDTO,
    var weather: List<DescriptionDTO>,
    var wind: WindDTO
)

@Serializable
data class TempDTO(var temp: Double)

@Serializable
data class DescriptionDTO(var description: String, var icon: String)

@Serializable
data class WindDTO(var speed: Double)