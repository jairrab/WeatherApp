package com.github.jairrab.weatherapp.ui.cities

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.jairrab.viewmodel.cities.CitiesViewModel
import com.github.jairrab.viewmodel.cities.event.CitiesEvent
import com.github.jairrab.viewmodel.utils.EventObserver
import com.github.jairrab.weatherapp.R
import com.github.jairrab.weatherapp.base.BaseFragment
import com.github.jairrab.weatherapp.databinding.CitiesBinding
import com.github.jairrab.weatherapp.ui.cities.adapter.CitiesAdapter
import com.github.jairrab.weatherapp.ui.city.CityFragment
import com.github.jairrab.weatherapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.WithFragmentBindings

@AndroidEntryPoint
@WithFragmentBindings
class CitiesFragment : BaseFragment(R.layout.cities) {
    private val binding by viewBinding { CitiesBinding.bind(it) }
    private val viewModel: CitiesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDisplayHomeAsUpEnabled(false)
        setupBottomSpinner()
        setupPullToRefresh()
        setupList()
        setupEventObserver()
    }

    private fun setupBottomSpinner() {
        viewModel.bottomSpinnerVisibilityLd.observe(viewLifecycleOwner) {
            binding.bottomSpinner.isVisible = it
        }
    }

    private fun setupPullToRefresh() {
        viewModel.swipeRefreshVisibilityLd.observe(viewLifecycleOwner) {
            binding.swipeRefresh.isRefreshing = false
        }

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.pullToRefresh()
        }
    }

    private fun setupList() {
        binding.recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CitiesAdapter {
                navigate(R.id.city, CityFragment.getArgs(it.cityId))
            }
        }

        viewModel.citiesLd.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as CitiesAdapter).submitList(it)
        }
    }

    private fun setupEventObserver() {
        viewModel.eventLd.observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is CitiesEvent.ApiErrorEvent -> showApiError(it.message)
                is CitiesEvent.ServerErrorEvent -> showServerError(it.message)
            }
        })
    }
}