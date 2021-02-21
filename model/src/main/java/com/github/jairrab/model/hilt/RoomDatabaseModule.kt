package com.github.jairrab.model.hilt

import android.app.Application
import com.github.jairrab.model.LocalDb
import com.github.jairrab.model.implementation.LocalDbImpl
import com.github.jairrab.model.room.RoomDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomDatabaseModule {
    @Provides
    fun providesLocalDb(
        application: Application,
    ): LocalDb {
        val database = RoomDb.getInstance(application)
        return LocalDbImpl(database)
    }
}