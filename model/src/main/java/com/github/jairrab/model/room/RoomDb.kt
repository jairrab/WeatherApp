package com.github.jairrab.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.github.jairrab.model.models.WeatherCity
import timber.log.Timber

@Database(
    entities = [
        WeatherCity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(RoomConverters::class)
abstract class RoomDb : RoomDatabase() {
    abstract val cityWeatherDb: CityWeatherDb

    companion object {
        private var roomDb: RoomDb? = null

        fun getInstance(context: Context): RoomDb {
            if (roomDb == null) {
                val appContext = context.applicationContext
                val clazz = RoomDb::class.java
                val dbName = "projects.db"

                roomDb = Room
                    .databaseBuilder(appContext, clazz, dbName)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Timber.v("Room db is created")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Timber.v("Room db is opened")
                        }
                    })
                    .build()
            }

            return roomDb as RoomDb
        }
    }
}