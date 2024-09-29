package com.daisy.videostreamapp.domain.repository

import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface VideoRepository {

    fun getVideos(): Flow<Resource<List<VideoItem>>>
}