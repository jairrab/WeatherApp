package com.github.jairrab.weatherapp.ui.city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.github.jairrab.viewmodel.city.CityViewModel
import com.github.jairrab.viewmodel.city.event.CityEvent
import com.github.jairrab.viewmodel.utils.EventObserver
import com.github.jairrab.weatherapp.R
import com.github.jairrab.weatherapp.base.BaseFragment
import com.github.jairrab.weatherapp.ui.theme.ComposePlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class CityFragment : BaseFragment() {
    private val viewModel: CityViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

        viewModel.eventLd.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is CityEvent.ApiErrorEvent -> showApiError(it.message)
                is CityEvent.ServerErrorEvent -> showServerError(it.message)
            }
        })

        setContent {
            ComposePlaygroundTheme() {
                CityDetails(viewModel)
            }
        }
    }

    companion object {
        fun getArgs(cityId: Int): Bundle {
            return Bundle().apply {
                putInt(CityViewModel.CITY_ID, cityId)
            }
        }
    }
}

@Composable
fun CityDetails(viewModel: CityViewModel) {
    Column(modifier = Modifier.padding(top = 100.dp, start = 24.dp, end = 24.dp)) {
        Row {
            CityName(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f),
                viewModel = viewModel
            )
            Favorite(viewModel)
        }
        Row(modifier = Modifier.padding(top = 24.dp)) {
            Temperature(
                modifier = Modifier
                    .width(0.dp)
                    .weight(0.8f)
                    .padding(end = 24.dp),
                viewModel = viewModel
            )
            Column(
                modifier = Modifier
                    .width(0.dp)
                    .weight(1f),
            ) {
                Weather(viewModel)
                HighLowTemp(viewModel)
            }
        }
    }
}

@Composable
fun Temperature(modifier: Modifier, viewModel: CityViewModel) {
    val text by viewModel.temperatureTextLd.observeAsState("")
    Text(text = text, modifier = modifier, fontSize = 36.sp, textAlign = TextAlign.End)
}

@Composable
fun CityName(modifier: Modifier, viewModel: CityViewModel) {
    val text by viewModel.nameTextLd.observeAsState("")
    Text(text = text, modifier = modifier, textAlign = TextAlign.Center, fontSize = 24.sp)
}

@Composable
fun Weather(viewModel: CityViewModel) {
    val text by viewModel.weatherTextLd.observeAsState("")
    Text(text = text, fontSize = 18.sp)
}

@Composable
fun HighLowTemp(viewModel: CityViewModel) {
    val text by viewModel.tempMinMaxTextLd.observeAsState("")
    Text(text = text, fontSize = 18.sp)
}

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