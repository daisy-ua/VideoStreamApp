package com.daisy.videostreamapp.domain.entity

import androidx.media3.common.MediaItem
import com.daisy.videostreamapp.data.remote.ApiPath

data class VideoItem(
    val description: String?,

    val subtitle: String,

    val sources: List<String>,

    val thumb: String?,

    val title: String,
) {
    val fullThumbUrl: String?
        get() = thumb?.let { "${ApiPath.IMAGE_BASE_URL}$it" }

    val mediaItem: List<MediaItem>
        get() = sources.map { uri -> MediaItem.fromUri(uri) }
}
