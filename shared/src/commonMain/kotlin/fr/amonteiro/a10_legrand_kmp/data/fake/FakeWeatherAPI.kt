package fr.amonteiro.a10_legrand_kmp.data.fake

import fr.amonteiro.a10_legrand_kmp.domain.WeatherRepository
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather

class FakeWeatherAPI : WeatherRepository {

    override suspend fun loadWeathers(city:String) = listOf(
        Weather(
            id = 1,
            name = "Paris",
            temp = 18.5,
            description = "ciel dégagé",
            icon = "https://picsum.photos/200",
            speed = 5.0
        ),
        Weather(
            id = 2,
            name = "Toulouse",
            temp = 22.3,
            description = "partiellement nuageux",
            icon = "https://picsum.photos/201",
            speed = 3.2
        )
    )

}