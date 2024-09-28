package com.daisy.videostreamapp.data.repository.mapper

import com.daisy.videostreamapp.data.remote.ApiPath
import com.daisy.videostreamapp.data.remote.model.VideoItemDto
import com.daisy.videostreamapp.domain.entity.VideoItem

//TODO: change to entity

internal fun List<VideoItemDto>.toEntity() = map { it.toEntity() }

internal fun VideoItemDto.toEntity() = VideoItem(
    id = 0L,
    description = description ?: "",
    subtitle = subtitle ?: "",
    sources = sources ?: emptyList(),
    thumb = thumb,
    title = title ?: "",
)