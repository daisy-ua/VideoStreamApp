package com.daisy.videostreamapp.data.repository.mapper

import com.daisy.videostreamapp.data.local.entity.VideoItemEntity
import com.daisy.videostreamapp.domain.entity.VideoItem

internal fun List<VideoItemEntity>.toDomain() = map { it.toDomain() }

internal fun VideoItemEntity.toDomain() = VideoItem(
    description = description,
    subtitle = subtitle,
    sources = sources,
    thumb = thumb,
    title = title,
)