package com.github.jairrab.repository.hilt

import com.github.jairrab.repository.Repository
import com.github.jairrab.repository.implementation.RepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun repository(repositoryImpl: RepositoryImpl): Repository
}