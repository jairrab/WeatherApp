package com.github.jairrab.weatherapp.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.jairrab.viewmodel.city.CityViewModel
import com.github.jairrab.weatherapp.ui.cities.Cities
import com.github.jairrab.weatherapp.ui.city.CityDetails
import com.github.jairrab.weatherapp.ui.theme.ComposePlaygroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                MainContent(rememberNavController())
            }
        }
    }
}

@Composable
fun MainContent(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "list",
    ) {
        composable("list") {
            Cities(hiltViewModel()) { city ->
                navController.navigate(
                    resId = navController.findDestination("details")!!.id,
                    args = bundleOf(CityViewModel.CITY_ID to city.cityId)
                )
            }
        }
        composable("details") {
            CityDetails(hiltViewModel())
        }
    }
}