package fr.amonteiro.a10_legrand_kmp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    var id: Int, //id d'un point météo
    var name: String,
    var temp: Double, //Température
    var speed: Double, //Vitesse du vent
    var description: String,
    var icon: String
) {
    fun getResume() = """
            Il fait $temp° à $name (id=$id) avec un vent de $speed m/s
            -Description : $description
            -Icône : $icon
        """.trimIndent()
}