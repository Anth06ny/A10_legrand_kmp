package fr.amonteiro.a10_legrand_kmp.domain

import fr.amonteiro.a10_legrand_kmp.domain.model.Weather

interface WeatherRepository {
    suspend fun loadWeathers(cityName: String): List<Weather>
}