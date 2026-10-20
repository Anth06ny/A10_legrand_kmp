package fr.amonteiro.a10_legrand_kmp.presentation.ui

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.amonteiro.a10_legrand_kmp.domain.model.Weather
import fr.amonteiro.a10_legrand_kmp.presentation.screens.PictureRowItem
import kotlinx.coroutines.launch

//Weather
@Composable
actual fun WeatherGallery(
    modifier: Modifier,
    urlList: List<Weather>,
    onPictureClick: (Weather) -> Unit
) {

    //État du scroll de la LazyRow
    val state = rememberLazyListState()
    //Scope pour lancer la coroutine
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        state = state,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    coroutineScope.launch {
                        state.scrollBy(-delta)
                    }
                },
            )
    ) {
        items(urlList.size) {
            PictureRowItem(
                data = urlList[it],
                onPictureClick = onPictureClick
            )
        }
    }
}