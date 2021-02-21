package com.github.jairrab.remote.hilt

import android.app.Application
import com.github.jairrab.remote.BuildConfig.DEBUG
import com.github.jairrab.remote.R
import com.github.jairrab.remote.WeatherMapApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.NONE
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun providesWeatherApi(application: Application): WeatherMapApi =
        Retrofit.Builder()
            .baseUrl(application.getString(R.string.base_url))
            .client(getOkHttpClient(application))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherMapApi::class.java)
            .apply { Timber.v("WeatherApi initialized") }

    private fun getOkHttpClient(
        application: Application,
    ) = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = if (DEBUG) BODY else NONE })
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
                .addQueryParameter("appid", application.getString(R.string.weather_map_api_key))
                .build()
            val request = original.newBuilder().url(url).build()
            chain.proceed(request)
        })
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()
}

