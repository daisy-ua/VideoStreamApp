package com.daisy.videostreamapp.data.repository.mapper

import com.daisy.videostreamapp.data.local.entity.VideoItemEntity
import com.daisy.videostreamapp.domain.entity.VideoItem

internal fun List<VideoItem>.toEntity() = map { it.toEntity() }

internal fun VideoItem.toEntity() = VideoItemEntity(
    id = 0L,
    description = description,
    subtitle = subtitle,
    sources = sources,
    thumb = thumb,
    title = title,
)