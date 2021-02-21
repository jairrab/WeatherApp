package com.github.jairrab.weatherapp.ui.cities.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.github.jairrab.model.models.WeatherCity
import com.github.jairrab.weatherapp.databinding.CityHolderBinding

class CitiesAdapter(
    private val onClick: (WeatherCity) -> Unit,
) : ListAdapter<WeatherCity, CityHolder>(A) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CityHolderBinding.inflate(inflater, parent, false)
        return CityHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.update(getItem(position))
    }

    companion object A : DiffUtil.ItemCallback<WeatherCity>() {
        override fun areItemsTheSame(oldItem: WeatherCity, newItem: WeatherCity): Boolean {
            return oldItem.cityId == newItem.cityId
        }

        override fun areContentsTheSame(oldItem: WeatherCity, newItem: WeatherCity): Boolean {
            return oldItem == newItem
        }
    }
}

