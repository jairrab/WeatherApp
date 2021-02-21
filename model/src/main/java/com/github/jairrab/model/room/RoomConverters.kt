package com.github.jairrab.model.room

import androidx.room.TypeConverter
import com.github.jairrab.model.models.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class RoomConverters {
    @TypeConverter
    fun dateToLong(value: Date): Long {
        return value.time
    }

    @TypeConverter
    fun longToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun weathersToString(value: List<Weather>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToWeathers(value: String): List<Weather> {
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(value, type)
    }
}
