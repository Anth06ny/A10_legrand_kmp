package fr.amonteiro.a10_legrand_kmp.presentation.screens

import a10_legrand_kmp.shared.generated.resources.Res
import a10_legrand_kmp.shared.generated.resources.error
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_TYPE_NORMAL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import fr.amonteiro.a10_legrand_kmp.presentation.ui.theme.AppTheme
import org.jetbrains.compose.resources.painterResource

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES or UI_MODE_TYPE_NORMAL)
@Composable
fun DetailScreenPreview() {
    AppTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DetailScreen(
                modifier = Modifier.padding(innerPadding),
                //jeu de données pour la Preview
                data = Weather(
                    id = 1,
                    name = "Nice",
                    temp = 18.5,
                    description = "ciel dégagé",
                    icon = "https://picsum.photos/200",
                    speed = 5.0
                )
            )
        }
    }
}

@Composable
fun DetailScreen(
    data: Weather,
    modifier: Modifier = Modifier,
    onBtBackClic : ()->Unit={}
) {

    Column(
        modifier = modifier.padding(8.dp)
    )  {
        Text(
            text = data.name,
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth()
        )

        AsyncImage(
            model = data.icon ,
            contentDescription = data.description ,
            contentScale = ContentScale.FillWidth,
            //Pour toto.png. Si besoin de choisir l'import pour la classe R, c'est celle de votre package
            //Image d'échec de chargement qui sera utilisé par la preview
            error = painterResource(Res.drawable.error),
            //Image d'attente.
            //placeholder = painterResource(R.drawable.toto),
            onError = { println(it) },
            modifier = Modifier.fillMaxWidth().weight(1f)
        )

        Text(
            text = data.getResume(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.size(16.dp))

        Button(
            onClick =  onBtBackClic,
            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            modifier = Modifier
                .padding(8.dp)
                .align(CenterHorizontally)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Localized description",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Retour")
        }
    }
}