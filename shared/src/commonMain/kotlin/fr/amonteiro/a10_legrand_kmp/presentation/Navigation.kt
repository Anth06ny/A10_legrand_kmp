package fr.amonteiro.a10_legrand_kmp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import fr.amonteiro.a10_legrand_kmp.presentation.screens.DetailScreen
import fr.amonteiro.a10_legrand_kmp.presentation.screens.SearchScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

class Routes {
    @Serializable
    data object SearchRoute : NavKey

    @Serializable
    data class DetailRoute(val weather: Weather) : NavKey
}

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Routes.SearchRoute::class, Routes.SearchRoute.serializer())
            subclass(Routes.DetailRoute::class, Routes.DetailRoute.serializer())
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    //La pile d'écrans : une simple liste, sauvegardée lors des rotations
    val backStack = rememberNavBackStack(navConfig, Routes.SearchRoute)
    val mainViewModel: MainViewModel = koinViewModel<MainViewModel>()

    NavDisplay(
        backStack = backStack,
        //Retour système (bouton ou geste)
        onBack = { backStack.removeLastOrNull() },
        modifier = modifier,
        entryProvider = entryProvider {

            //Route 1 vers notre SearchScreen
            entry<Routes.SearchRoute> {
                SearchScreen(
                    mainViewModel = mainViewModel,
                    onPictureClick = { backStack.add(Routes.DetailRoute(it)) }
                )
            }

            //Route 2 vers un écran de détail, la route est reçue déjà typée
            entry<Routes.DetailRoute> { detailRoute ->
                DetailScreen(
                    data = detailRoute.weather,
                    onBtBackClic = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}
