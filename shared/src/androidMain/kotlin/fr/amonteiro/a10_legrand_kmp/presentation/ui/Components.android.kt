package fr.amonteiro.a10_legrand_kmp.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import fr.amonteiro.a10_legrand_kmp.presentation.screens.PictureRowItem

@Composable
actual fun WeatherGallery(
    modifier: Modifier,
    urlList: List<Weather>,
    onPictureClick: (Weather) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(urlList.size) {
            PictureRowItem(
                data = urlList[it],
                onPictureClick = onPictureClick
            )
        }
    }
}