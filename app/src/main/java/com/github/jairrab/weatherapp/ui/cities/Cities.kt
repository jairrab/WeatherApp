package com.github.jairrab.weatherapp.ui.cities

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.viewmodel.cities.CitiesViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun Cities(viewModel: CitiesViewModel, action: (WeatherCity) -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxSize(1f)) {
        val data by viewModel.citiesLd.observeAsState(emptyList())
        val spinnerVisibility by viewModel.bottomSpinnerVisibilityLd.observeAsState(false)
        val swipeRefreshVisibility by viewModel.swipeRefreshVisibilityLd.observeAsState(false)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshVisibility),
            modifier = Modifier.constrainAs(createRef()) {
                top.linkTo(parent.top, margin = 16.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.fillToConstraints
                width = Dimension.fillToConstraints
            },
            onRefresh = { viewModel.pullToRefresh() }
        ) {
            LazyColumn {
                items(data) {
                    WeatherRow(it, action)
                }
            }
        }

        if (spinnerVisibility) {
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(createRef()) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}