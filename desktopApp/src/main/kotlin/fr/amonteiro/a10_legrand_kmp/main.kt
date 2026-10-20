package fr.amonteiro.a10_legrand_kmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import fr.amonteiro.a10_legrand_kmp.di.initKoin

fun main() = application {

    initKoin()

    
    Window(
        onCloseRequest = ::exitApplication,
        title = "A10_legrand_kmp",
    ) {
        App()
    }
}