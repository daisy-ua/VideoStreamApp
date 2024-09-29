package com.daisy.videostreamapp.data.repository.mapper

import com.daisy.videostreamapp.data.remote.model.VideoItemDto
import com.daisy.videostreamapp.domain.entity.VideoItem

internal fun List<VideoItemDto>.toDomain() = map { it.toDomain() }

internal fun VideoItemDto.toDomain() = VideoItem(
    description = description ?: "",
    subtitle = subtitle ?: "",
    sources = sources ?: emptyList(),
    thumb = thumb,
    title = title ?: "",
)