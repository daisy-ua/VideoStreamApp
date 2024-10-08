package com.daisy.videostreamapp.data.repository

import android.util.Log
import com.daisy.videostreamapp.data.local.dao.VideoDao
import com.daisy.videostreamapp.data.remote.service.VideoService
import com.daisy.videostreamapp.data.repository.mapper.toDomain
import com.daisy.videostreamapp.data.repository.mapper.toEntity
import com.daisy.videostreamapp.domain.entity.VideoItem
import com.daisy.videostreamapp.domain.repository.VideoRepository
import com.daisy.videostreamapp.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoRepositoryImpl @Inject constructor(
    private val videoService: VideoService,
    private val videoDao: VideoDao,
) : VideoRepository {

    override fun getVideos(): Flow<Resource<List<VideoItem>>> = flow {
        try {
            emit(Resource.Loading())

            val mediaResponse = videoService.getMediaRaw()

            val videos = mediaResponse.videos.toDomain()

            videoDao.insertAll(videos.toEntity())

            emit(Resource.Success(videos))
        } catch (e: Exception) {
            Log.e("VideoRepositoryImpl", "Error fetching videos: ${e.message}", e)

            emit(Resource.Error(e, videoDao.getAllVideos().toDomain()))
        }
    }.flowOn(Dispatchers.IO)
}