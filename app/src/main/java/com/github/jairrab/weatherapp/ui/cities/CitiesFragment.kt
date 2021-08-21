package com.github.jairrab.weatherapp.ui.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.fragment.app.viewModels
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.viewmodel.cities.CitiesViewModel
import com.github.jairrab.viewmodel.cities.event.CitiesEvent
import com.github.jairrab.viewmodel.utils.EventObserver
import com.github.jairrab.weatherapp.R
import com.github.jairrab.weatherapp.base.BaseFragment
import com.github.jairrab.weatherapp.ui.city.CityFragment
import com.github.jairrab.weatherapp.ui.theme.ComposePlaygroundTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class CitiesFragment : BaseFragment() {
    private val viewModel: CitiesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setDisplayHomeAsUpEnabled(false)

        viewModel.eventLd.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is CitiesEvent.ApiErrorEvent -> showApiError(it.message)
                is CitiesEvent.ServerErrorEvent -> showServerError(it.message)
            }
        })

        setContent {
            ComposePlaygroundTheme() {
                Cities(viewModel) {
                    navigate(R.id.city, CityFragment.getArgs(it.cityId))
                }
            }
        }
    }
}


@Composable
fun Cities(viewModel: CitiesViewModel, action: (WeatherCity) -> Unit) {
    ConstraintLayout {
        val (list, spinner) = createRefs()

        val data by viewModel.citiesLd.observeAsState(emptyList())
        val spinnerVisibility by viewModel.bottomSpinnerVisibilityLd.observeAsState(false)
        val swipeRefreshVisibility by viewModel.swipeRefreshVisibilityLd.observeAsState(false)

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = swipeRefreshVisibility),
            modifier = Modifier.constrainAs(list) {
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
                modifier = Modifier.constrainAs(spinner) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
        }
    }
}

@Composable
fun WeatherRow(city: WeatherCity, action: (WeatherCity) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { action(city) }
            .fillMaxWidth()
            .shadow(6.dp)
            .background(Color(city.getBackgroundColor()), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = city.temperature.toString(),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f)
            )
            if (city.isFavorite) {
                Image(
                    painterResource(R.drawable.ic_baseline_favorite_24),
                    contentDescription = ""
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = city.cityName,
                fontSize = 24.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = city.weatherCondition.joinToString { it.main },
                fontSize = 20.sp,
            )
        }
    }
}

private fun WeatherCity.getBackgroundColor() = when {
    temperature < 0 -> "#1976D2"
    temperature >= 0 && temperature < 15 -> "#26C6DA"
    temperature >= 15 && temperature < 30 -> "#66BB6A"
    else -> "#FF7043"
}.let { android.graphics.Color.parseColor(it) }