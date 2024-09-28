package com.daisy.videostreamapp.data.remote.service

import com.daisy.videostreamapp.data.remote.ApiPath
import com.daisy.videostreamapp.data.remote.model.MediaResponse
import retrofit2.http.GET

interface VideoService {

    @GET(ApiPath.MEDIA_RAW)
    suspend fun getMediaRaw(): MediaResponse
}