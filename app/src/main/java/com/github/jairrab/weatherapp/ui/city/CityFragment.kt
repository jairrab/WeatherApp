package com.github.jairrab.weatherapp.ui.city

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.github.jairrab.viewmodel.cities.event.CitiesEvent
import com.github.jairrab.viewmodel.city.CityViewModel
import com.github.jairrab.viewmodel.city.event.CityEvent
import com.github.jairrab.viewmodel.utils.EventObserver
import com.github.jairrab.weatherapp.R
import com.github.jairrab.weatherapp.base.BaseFragment
import com.github.jairrab.weatherapp.databinding.CityBinding
import com.github.jairrab.weatherapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings
import timber.log.Timber

@AndroidEntryPoint
@WithFragmentBindings
class CityFragment : BaseFragment(R.layout.city) {
    private val binding by viewBinding { CityBinding.bind(it) }
    private val viewModel: CityViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)
        setupName()
        setupTemperature()
        setupWeather()
        setupFavorite()
        setupEventObserver()
    }

    private fun setupName() {
        viewModel.nameTextLd.observe(viewLifecycleOwner) {
            binding.name.text = it
        }
    }

    private fun setupTemperature() {
        viewModel.temperatureTextLd.observe(viewLifecycleOwner) {
            binding.temperature.text = it
        }

        viewModel.tempMinMaxTextLd.observe(viewLifecycleOwner) {
            binding.highLow.text = it
        }
    }

    private fun setupWeather() {
        viewModel.weatherTextLd.observe(viewLifecycleOwner) {
            binding.weather.text = it
        }
    }

    private fun setupFavorite() {
        viewModel.isFavoriteLd.observe(viewLifecycleOwner) {
            Timber.v("Favorite value is $it")
            val resId = if (it) R.drawable.ic_baseline_favorite_24 else {
                R.drawable.ic_baseline_favorite_border_24
            }
            binding.favorite.setImageResource(resId)
        }

        binding.favorite.setOnClickListener {
            viewModel.toggleFavorite()
        }
    }

    private fun setupEventObserver() {
        viewModel.eventLd.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is CityEvent.ApiErrorEvent -> showApiError(it.message)
                is CityEvent.ServerErrorEvent -> showServerError(it.message)
            }
        })
    }

    companion object {
        fun getArgs(cityId: Int): Bundle {
            return Bundle().apply {
                putInt(CityViewModel.CITY_ID, cityId)
            }
        }
    }
}