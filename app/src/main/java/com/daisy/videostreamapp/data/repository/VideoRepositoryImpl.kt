package com.daisy.videostreamapp.data.repository

import com.daisy.videostreamapp.data.remote.service.VideoService
import com.daisy.videostreamapp.data.repository.mapper.toEntity
import com.daisy.videostreamapp.domain.entity.VideoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val videoService: VideoService,
) : VideoRepository {

    override fun getVideos(): Flow<List<VideoItem>> = flow {
        try {
            val mediaResponse = videoService.getMediaRaw()
            val videos = mediaResponse.videos.toEntity()
            emit(videos)
        } catch (e: Exception) {
            emit(listOf())
        }
    }
}