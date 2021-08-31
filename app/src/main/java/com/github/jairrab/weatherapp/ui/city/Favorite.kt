package com.github.jairrab.weatherapp.ui.city

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.github.jairrab.viewmodel.city.CityViewModel
import com.github.jairrab.weatherapp.R

@Composable
fun Favorite(viewModel: CityViewModel) {
    val favorite by viewModel.isFavoriteLd.observeAsState(false)
    Image(
        painterResource(
            id = if (favorite) {
                R.drawable.ic_baseline_favorite_24
            } else R.drawable.ic_baseline_favorite_border_24
        ),
        contentDescription = null,
        modifier = Modifier.clickable { viewModel.toggleFavorite() }
    )
}