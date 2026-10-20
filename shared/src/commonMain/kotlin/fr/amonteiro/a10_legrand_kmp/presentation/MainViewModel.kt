package fr.amonteiro.a10_legrand_kmp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.amonteiro.a10_legrand_kmp.di.initKoin
import fr.amonteiro.a10_legrand_kmp.domain.WeatherRepository
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform

suspend fun main() {

    initKoin()
    val viewModel = KoinPlatform.getKoin().get<MainViewModel>()
    viewModel.loadWeathers("Nice")

    while (viewModel.runInProgress.value) {
        delay(500)
        println("Attente...")
    }

    //Affichage de la liste contenue dans la donnée observable
    println("List : ${viewModel.dataList.value}")

}

class MainViewModel(val weatherApiDataSource: WeatherRepository) : ViewModel() {
    //MutableStateFlow est une donnée observable
    val dataList = MutableStateFlow(emptyList<Weather>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {
        println("Instanciation de MainViewModel")
        //loadFakeData()
    }


    fun loadWeathers(cityName: String) {
        runInProgress.value = true
        errorMessage.value = ""

        viewModelScope.launch(Dispatchers.IO) {
            try {
                dataList.value = weatherApiDataSource.loadWeathers(cityName)
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage.value = e.message ?: "Une erreur est survenue"
                launch(Dispatchers.Default) {
                    delay(4000)
                    errorMessage.value = ""
                }
            } finally {
                runInProgress.value = false
            }
        }
    }

    fun loadFakeData(runInProgress: Boolean = false, errorMessage: String = "") {
        this.runInProgress.value = runInProgress
        this.errorMessage.value = errorMessage
        dataList.value = listOf(
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
            ),
            Weather(
                id = 3,
                name = "Toulon",
                temp = 25.1,
                description = "ensoleillé",
                icon = "https://picsum.photos/202",
                speed = 6.7
            ),
            Weather(
                id = 4,
                name = "Lyon",
                temp = 19.8,
                description = "pluie légère",
                icon = "https://picsum.photos/203",
                speed = 4.5
            )
        ).shuffled() //shuffled() pour avoir un ordre différent à chaque appel
    }

}