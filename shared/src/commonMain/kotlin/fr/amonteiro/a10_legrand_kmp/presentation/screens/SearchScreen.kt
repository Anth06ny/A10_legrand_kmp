package fr.amonteiro.a10_legrand_kmp.presentation.screens

import a10_legrand_kmp.shared.generated.resources.Res
import a10_legrand_kmp.shared.generated.resources.bt_load
import a10_legrand_kmp.shared.generated.resources.error
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import fr.amonteiro.a10_legrand_kmp.di.fakeApiModule
import fr.amonteiro.a10_legrand_kmp.di.viewModelModule
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import fr.amonteiro.a10_legrand_kmp.presentation.MainViewModel
import fr.amonteiro.a10_legrand_kmp.presentation.ui.MyError
import fr.amonteiro.a10_legrand_kmp.presentation.ui.WeatherGallery
import fr.amonteiro.a10_legrand_kmp.presentation.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Preview(showBackground = true, showSystemUi = true, name = "Version de base", locale = "fr")
@Composable
fun SearchScreenWithDataPreview() {
    //Il faut remplacer NomVotreProjetTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    KoinApplicationPreview(application = {
        modules(viewModelModule, fakeApiModule)
    }) {
        AppTheme {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                //koinViewModel() et non viewModel() car MainViewModel attend un WeatherRepository
                val mainViewModel: MainViewModel = koinViewModel()
                mainViewModel.loadWeathers("Paris")
                SearchScreen(
                    modifier = Modifier.padding(innerPadding),
                    mainViewModel = mainViewModel
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Version de base", locale = "fr")
@Preview(
    showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL
)
@Composable
fun SearchScreenPreview() {
    //Il faut remplacer NomVotreProjetTheme par le thème de votre application
    //Utilisé par exemple dans MainActivity.kt sous setContent {...}
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            SearchScreenContent(
                modifier = Modifier.padding(innerPadding),
                list = emptyList<Weather>(),
                runInProgress = false,
                errorMessage = "Une erreur",
                searchText = "Coucou"
            )
        }
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    onPictureClick: (Weather) -> Unit = {}
) {

    var searchText by remember { mutableStateOf("") }

//    LaunchedEffect("") {
//        mainViewModel.loadWeathers("Toulouse")
//    }

    SearchScreenContent(
        modifier = modifier,
        list = mainViewModel.dataList.collectAsStateWithLifecycle().value,
        runInProgress = mainViewModel.runInProgress.collectAsStateWithLifecycle().value,
        errorMessage = mainViewModel.errorMessage.collectAsStateWithLifecycle().value,
        searchText = searchText,
        onSearchAction = { mainViewModel.loadWeathers(searchText) },
        onBtClearClick = { searchText = "" },
        onPictureClick = onPictureClick,
        onBtLoadClick = { mainViewModel.loadWeathers(searchText) },
        onValueChange = { searchText = it }

    )
}


@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    list: List<Weather>,
    runInProgress: Boolean,
    errorMessage: String,
    searchText: String,
    onPictureClick: (Weather) -> Unit = {},
    onSearchAction: () -> Unit = {},
    onValueChange: (String) -> Unit = {},
    onBtClearClick: () -> Unit = {},
    onBtLoadClick: () -> Unit = {},

    ) {

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchBar(
            searchText = searchText,
            onSearchAction = onSearchAction,
            onValueChange = onValueChange
        )

        MyError(errorMessage = errorMessage)

        AnimatedVisibility(runInProgress) {
            CircularProgressIndicator()
        }

        WeatherGallery(
            modifier = Modifier.weight(2f),
            urlList = list,
            onPictureClick = onPictureClick
        )


        Row {
            Button(
                onClick = onBtClearClick,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Clear Filter")
            }

            Button(
                onClick = onBtLoadClick,
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(Res.string.bt_load))
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onValueChange: (String) -> Unit = {},
    onSearchAction: () -> Unit = {}
) {

    TextField(
        value = searchText, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icône
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans strings.xml, R de votre package com.nom.projet
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        keyboardActions = KeyboardActions(onSearch = { onSearchAction() }), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}


@Composable
fun PictureRowItem(
    modifier: Modifier = Modifier,
    data: Weather,
    onPictureClick: (Weather) -> Unit
) {

    var expended by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .clickable {
                onPictureClick(data)
            }
            .fillMaxWidth()
    ) {

        //Permission Internet nécessaire
        AsyncImage(
            model = data.icon,
            //Pour aller le chercher dans strings.xml R de votre package com.nom.projet
            //contentDescription = stringResource(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            contentScale = ContentScale.FillWidth,

            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(Res.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),

            onError = { println(it) },
            modifier = Modifier
                .heightIn(max = 100.dp)
                .widthIn(max = 100.dp)
        )

        Column(modifier = Modifier.clickable {
            expended = !expended
        }) {
            Text(
                text = data.name,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            Text(
                text = if (!expended) data.getResume().take(20) + "..." else data.getResume(),
            )
        }
    }
}

