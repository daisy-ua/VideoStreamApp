package com.daisy.videostreamapp.di

import com.daisy.videostreamapp.data.repository.VideoRepository
import com.daisy.videostreamapp.data.repository.VideoRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@InstallIn(ViewModelComponent::class)
@Module
interface RepositoryModule {
    @Binds
    @ViewModelScoped
    fun bindVideoRepository(
        impl: VideoRepositoryImpl,
    ): VideoRepository
}