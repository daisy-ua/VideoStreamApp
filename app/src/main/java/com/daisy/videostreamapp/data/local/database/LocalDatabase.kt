package com.daisy.videostreamapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.daisy.videostreamapp.data.local.converter.StringListConverter
import com.daisy.videostreamapp.data.local.dao.VideoDao
import com.daisy.videostreamapp.data.local.entity.VideoItemEntity

@Database(
    entities = [VideoItemEntity::class],
    version = 1
)
@TypeConverters(StringListConverter::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun videoDao(): VideoDao
}