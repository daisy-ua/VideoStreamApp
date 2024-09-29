package com.daisy.videostreamapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.daisy.videostreamapp.data.local.entity.VideoItemEntity

@Dao
interface VideoDao {
    @Query("SELECT * FROM video")
    suspend fun getAllVideos(): List<VideoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(videos: List<VideoItemEntity>)
}