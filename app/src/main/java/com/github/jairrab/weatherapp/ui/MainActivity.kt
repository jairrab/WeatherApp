package com.github.jairrab.weatherapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.jairrab.weatherapp.databinding.ActivityMainBinding
import com.github.jairrab.weatherapp.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by viewBinding { ActivityMainBinding.inflate(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}