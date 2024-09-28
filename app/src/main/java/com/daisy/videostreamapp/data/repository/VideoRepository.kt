package com.daisy.videostreamapp.data.repository

import com.daisy.videostreamapp.domain.entity.VideoItem
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(): Flow<List<VideoItem>>
}