package com.daisy.videostreamapp.di

import android.content.Context
import androidx.room.Room
import com.daisy.videostreamapp.data.local.dao.VideoDao
import com.daisy.videostreamapp.data.local.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): LocalDatabase {
//        appContext.deleteDatabase("videostream.db")
        return Room.databaseBuilder(
            appContext,
            LocalDatabase::class.java,
            "videostream.db"
        ).build()
    }

    @Provides
    fun provideVideoDao(database: LocalDatabase): VideoDao = database.videoDao()
}